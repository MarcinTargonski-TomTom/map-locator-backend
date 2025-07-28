package com.tomtom.locator.map.map_locator.mok.exception;

import org.springframework.http.HttpStatus;

import static com.tomtom.locator.map.map_locator.mok.exception.MokExceptionMessage.INVALID_CREDENTIALS_EXCEPTION;

public class InvalidCredentialsException extends MokBaseException {

    public InvalidCredentialsException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public static InvalidCredentialsException withDefaultMsg() {
        return new InvalidCredentialsException(INVALID_CREDENTIALS_EXCEPTION);
    }
}
