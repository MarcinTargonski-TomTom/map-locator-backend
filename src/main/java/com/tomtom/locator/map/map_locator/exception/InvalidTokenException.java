package com.tomtom.locator.map.map_locator.exception;

import org.springframework.http.HttpStatus;

import static com.tomtom.locator.map.map_locator.exception.ExceptionMessage.INVALID_TOKEN_EXCEPTION;

public class InvalidTokenException extends AppBaseException {
    public InvalidTokenException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(HttpStatus.UNAUTHORIZED, message, cause);
    }

    public static InvalidTokenException withDefaultMsgAndCause(Exception cause) {
        return new InvalidTokenException(INVALID_TOKEN_EXCEPTION, cause);
    }

    public static InvalidTokenException withDefaultMsg() {
        return new InvalidTokenException(INVALID_TOKEN_EXCEPTION);
    }
}
