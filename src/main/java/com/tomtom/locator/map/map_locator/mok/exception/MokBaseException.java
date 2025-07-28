package com.tomtom.locator.map.map_locator.mok.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

abstract class MokBaseException extends ResponseStatusException {

    public MokBaseException(HttpStatusCode status, String message) {
        super(status, message);
    }

    public MokBaseException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
