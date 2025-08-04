package com.tomtom.locator.map.map_locator.exception;

import org.springframework.http.HttpStatus;

public class EmptyListException extends AppBaseException {
    public EmptyListException() {
        super(HttpStatus.NOT_FOUND, "");
    }
}
