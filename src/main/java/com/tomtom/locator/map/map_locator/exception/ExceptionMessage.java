package com.tomtom.locator.map.map_locator.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessage {
    public static final String INVALID_CREDENTIALS_EXCEPTION = "Invalid login or password";
    public static final String ACCOUNT_NOT_ACTIVE_EXCEPTION = "Account not active";
    public static final String INVALID_TOKEN_EXCEPTION = "Token is invalid";
    public static final String EXPIRED_TOKEN_EXCEPTION = "Token is expired";
    public static final String UNKNOWN = "Unknown problem, we will fix it soon";
    public static final String VALIDATION_FAILED = "Validation failed";
    public static final String MALFORMED_JSON = "Malformed JSON request or missing body";
    public static final String EXTERNAL_SERVICE_CLIENT_ERROR = "Couldn't fetch required data from external service";
    public static final String EXTERNAL_SERVICE_SERVER_ERROR = "Couldn't fetch required data from external service";
}
