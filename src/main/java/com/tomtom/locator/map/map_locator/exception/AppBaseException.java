package com.tomtom.locator.map.map_locator.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

abstract class AppBaseException extends ResponseStatusException {

    public AppBaseException(HttpStatusCode status, String message) {
        super(status, message);
    }

    public AppBaseException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
