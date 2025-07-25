package com.tomtom.locator.map.map_locator.mok.exception;

public class MokExceptionMessage {

    private MokExceptionMessage() {
    }

    public static final String INVALID_CREDENTIALS_EXCEPTION = "Invalid login or password";
    public static final String ACCOUNT_NOT_ENABLED_EXCEPTION = "Account not enabled";
    public static final String INVALID_TOKEN_EXCEPTION = "Token is invalid";
    public static final String EXPIRED_TOKEN_EXCEPTION = "Token is expired";
}
