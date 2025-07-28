package com.tomtom.locator.map.map_locator.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessage {
    public static final String INVALID_CREDENTIALS_EXCEPTION = "Invalid login or password";
    public static final String ACCOUNT_NOT_ACTIVE_EXCEPTION = "Account not active";
    public static final String INVALID_TOKEN_EXCEPTION = "Token is invalid";
    public static final String EXPIRED_TOKEN_EXCEPTION = "Token is expired";
}
