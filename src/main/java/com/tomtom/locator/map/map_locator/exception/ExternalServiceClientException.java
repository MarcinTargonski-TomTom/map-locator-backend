package com.tomtom.locator.map.map_locator.exception;

import org.springframework.http.HttpStatus;

public class ExternalServiceClientException extends AppBaseException {

    public ExternalServiceClientException(String reason) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, reason);
    }

    public static ExternalServiceClientException withDefaultMsg() {
        return new ExternalServiceClientException(ExceptionMessage.EXTERNAL_SERVICE_CLIENT_ERROR);
    }
}
