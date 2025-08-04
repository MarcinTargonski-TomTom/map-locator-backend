package com.tomtom.locator.map.map_locator.exception;

import org.springframework.http.HttpStatus;

public class UnsupportedGeometryTypeException extends AppBaseException {
    public UnsupportedGeometryTypeException(String geometryType) {
        super(HttpStatus.NOT_IMPLEMENTED, "Unsupported geometry type: " + geometryType);
    }
}
