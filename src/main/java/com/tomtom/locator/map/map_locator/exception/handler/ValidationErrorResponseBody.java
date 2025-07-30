package com.tomtom.locator.map.map_locator.exception.handler;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.NonNull;
import org.springframework.http.HttpStatusCode;

import java.time.ZonedDateTime;
import java.util.List;

record ValidationErrorResponseBody(
        @JsonUnwrapped @NonNull
        ExceptionResponseBody exception,
        @NonNull List<FieldValidationError> fieldErrors
) {

    ValidationErrorResponseBody(
            @NonNull ZonedDateTime timestamp,
            @NonNull String path,
            @NonNull HttpStatusCode statusCode,
            @NonNull String message,
            @NonNull List<FieldValidationError> fieldErrors
    ) {
        this(new ExceptionResponseBody(timestamp, path, statusCode, message), fieldErrors);
    }
}