package com.tomtom.locator.map.map_locator.exception;

import org.springframework.http.HttpStatus;

import static com.tomtom.locator.map.map_locator.exception.ExceptionMessage.ANY_COORDINATES_GIVEN_EXCEPTION;

public class AnyCoordinatesGivenException extends AppBaseException {
    public AnyCoordinatesGivenException() {
        super(HttpStatus.BAD_REQUEST, ANY_COORDINATES_GIVEN_EXCEPTION);
    }
}
