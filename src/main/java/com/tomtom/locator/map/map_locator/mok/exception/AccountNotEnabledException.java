package com.tomtom.locator.map.map_locator.mok.exception;

import static com.tomtom.locator.map.map_locator.mok.exception.MokExceptionMessage.ACCOUNT_NOT_ENABLED_EXCEPTION;

public class AccountNotEnabledException extends RuntimeException {

    public AccountNotEnabledException(String message) {
        super(message);
    }

    public static AccountNotEnabledException withDefaultMsg() {
        return new AccountNotEnabledException(ACCOUNT_NOT_ENABLED_EXCEPTION);
    }

}
