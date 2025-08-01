package com.tomtom.locator.map.map_locator.exception.handler;

import com.tomtom.locator.map.map_locator.exception.AppBaseException;
import com.tomtom.locator.map.map_locator.exception.ExceptionMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
class GeneralExceptionHandler {

    @ExceptionHandler(Exception.class)
    ResponseEntity<ExceptionResponseBody> handleUnexpectedError(Exception e, HttpServletRequest request) {
        log.error("Unexpected exception occurred: ", e);

        ExceptionResponseBody responseBody = new ExceptionResponseBody(
                ZonedDateTime.now(),
                request.getServletPath(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ExceptionMessage.UNKNOWN
        );

        return ResponseEntity
                .internalServerError()
                .body(responseBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ValidationErrorResponseBody> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<FieldValidationError> fieldErrors = getFieldValidationErrors(ex);
        log.warn("Validation failed on request to {}: {}", request.getServletPath(), fieldErrors);

        return ResponseEntity
                .badRequest()
                .body(new ValidationErrorResponseBody(
                        ZonedDateTime.now(),
                        request.getServletPath(),
                        HttpStatus.BAD_REQUEST,
                        ExceptionMessage.VALIDATION_FAILED,
                        fieldErrors
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ExceptionResponseBody> handleMalformedJson(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("Malformed JSON request", ex);

        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponseBody(
                        ZonedDateTime.now(),
                        request.getServletPath(),
                        HttpStatus.BAD_REQUEST,
                        ExceptionMessage.MALFORMED_JSON
                ));
    }

    @ExceptionHandler(AppBaseException.class)
    public ResponseEntity<ExceptionResponseBody> handleAppExceptions(AppBaseException ex, HttpServletRequest request) {
        log.info("Application exception caught: ", ex);
        ExceptionResponseBody body = new ExceptionResponseBody(
                ZonedDateTime.now(),
                request.getServletPath(),
                ex.getStatusCode(),
                ex.getMessage()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }



    private static List<FieldValidationError> getFieldValidationErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldValidationError(error.getField(), error.getDefaultMessage()))
                .toList();
    }
}