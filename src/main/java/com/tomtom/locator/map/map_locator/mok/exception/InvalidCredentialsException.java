package com.tomtom.locator.map.map_locator.mok.exception;

import static com.tomtom.locator.map.map_locator.mok.exception.MokExceptionMessage.INVALID_CREDENTIALS_EXCEPTION;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public static InvalidCredentialsException withDefaultMsg() {
        return new InvalidCredentialsException(INVALID_CREDENTIALS_EXCEPTION);
    }
}
