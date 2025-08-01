package com.tomtom.locator.map.map_locator.exception.handler;

import org.springframework.http.HttpStatusCode;

import java.time.ZonedDateTime;

record ExceptionResponseBody(
    ZonedDateTime timestamp,
    String path,
    HttpStatusCode statusCode,
    String message
) {
}
