package com.tomtom.locator.map.map_locator.exception;

import org.springframework.http.HttpStatus;

public class ExternalServiceServerException extends AppBaseException {

    public ExternalServiceServerException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static ExternalServiceServerException withDefaultMsg() {
        return new ExternalServiceServerException(ExceptionMessage.EXTERNAL_SERVICE_SERVER_ERROR);
    }
}
