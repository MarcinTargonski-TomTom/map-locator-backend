package com.tomtom.locator.map.map_locator.mok.exception;

import static com.tomtom.locator.map.map_locator.mok.exception.MokExceptionMessage.EXPIRED_TOKEN_EXCEPTION;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException(String message) {
        super(message);
    }

    public ExpiredTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ExpiredTokenException withDefaultMsgAndCause(Exception cause) {
        return new ExpiredTokenException(EXPIRED_TOKEN_EXCEPTION, cause);
    }
}
