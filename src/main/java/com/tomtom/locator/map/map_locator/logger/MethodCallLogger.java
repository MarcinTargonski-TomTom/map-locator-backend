package com.tomtom.locator.map.map_locator.logger;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.Arrays;

import static com.tomtom.locator.map.map_locator.logger.LogColor.RED;
import static com.tomtom.locator.map.map_locator.logger.LogColor.RESET;
import static com.tomtom.locator.map.map_locator.logger.LogColor.YELLOW;

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class MethodCallLogger {

    public static final ThreadLocal<StringBuilder> logBuilder = ThreadLocal.withInitial(StringBuilder::new);


    private static final Logger log = LoggerFactory.getLogger(MethodCallLogger.class);
    private static final ThreadLocal<Integer> callDepth = ThreadLocal.withInitial(() -> 0);
    private static final String INDENT = "    ";

    public static String indent() {
        return INDENT.repeat(callDepth.get());
    }

    public static void appendLog(String message) {
        logBuilder.get().append(indent()).append(message).append("\n");
    }

    public static void appendLog(String message, String color) {
        logBuilder.get().append(indent()).append(color).append(message).append(RESET).append("\n");
    }

    private static void logCompleteRequest() {
        appendLog(Instant.now().toString());
        appendLog("=== REQUEST END ===", YELLOW);
        log.info(logBuilder.get().toString());

        callDepth.remove();
        logBuilder.get().setLength(0);
    }


    @Before("within(@org.springframework.web.bind.annotation.RestController *)")
    private void logControllerStart(JoinPoint joinPoint) {
        appendLog("\n=== REQUEST START ===", YELLOW);
        appendLog(Instant.now().toString());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = auth != null ? auth.getName() : "anonymous";
        appendLog("User: " + user);


        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        appendLog("Endpoint: " + request.getMethod() + " " + request.getRequestURI());
        appendLog(joinPoint.getSignature().toShortString() + " Arguments: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "within(@org.springframework.web.bind.annotation.RestController *) || within(@org.springframework.web.bind.annotation.RestControllerAdvice *)", returning = "result")
    private void logControllerEnd(Object result) {
        appendLog("Response: " + result);
        logCompleteRequest();
    }


    @Around("within(@com.tomtom.locator.map.map_locator.loggers.MethodCallLogged *)")
    private Object logServiceAndRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        callDepth.set(callDepth.get() + 1);

        String methodName = joinPoint.getSignature().toShortString();
        appendLog("Method: " + methodName + " Arguments: " + Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            appendLog("Returned: " + result);
            decreaseCallDepth();
            return result;
        } catch (Exception e) {
            appendLog("Exception in method " + methodName + ": " + e.getClass(), RED);
            decreaseCallDepth();
            e.printStackTrace();
            decreaseCallDepth();
            throw e;
        }

    }

    private void decreaseCallDepth() {
        if (callDepth.get() > 0) {
            callDepth.set(callDepth.get() - 1);
        }
    }
}
