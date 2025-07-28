package com.tomtom.locator.map.map_locator.exception;

import org.springframework.http.HttpStatus;

import static com.tomtom.locator.map.map_locator.exception.ExceptionMessage.EXPIRED_TOKEN_EXCEPTION;

public class ExpiredTokenException extends AppBaseException {
    public ExpiredTokenException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public ExpiredTokenException(String message, Throwable cause) {
        super(HttpStatus.UNAUTHORIZED, message, cause);
    }

    public static ExpiredTokenException withDefaultMsgAndCause(Exception cause) {
        return new ExpiredTokenException(EXPIRED_TOKEN_EXCEPTION, cause);
    }
}
