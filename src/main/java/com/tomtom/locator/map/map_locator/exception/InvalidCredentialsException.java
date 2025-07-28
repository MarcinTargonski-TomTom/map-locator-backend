package com.tomtom.locator.map.map_locator.exception;

import org.springframework.http.HttpStatus;

import static com.tomtom.locator.map.map_locator.exception.ExceptionMessage.INVALID_CREDENTIALS_EXCEPTION;

public class InvalidCredentialsException extends AppBaseException {

    public InvalidCredentialsException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public static InvalidCredentialsException withDefaultMsg() {
        return new InvalidCredentialsException(INVALID_CREDENTIALS_EXCEPTION);
    }
}
