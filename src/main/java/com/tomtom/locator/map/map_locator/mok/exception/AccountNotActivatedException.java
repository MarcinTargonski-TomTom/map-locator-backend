package com.tomtom.locator.map.map_locator.mok.exception;

import org.springframework.http.HttpStatus;

import static com.tomtom.locator.map.map_locator.mok.exception.MokExceptionMessage.ACCOUNT_NOT_ENABLED_EXCEPTION;

public class AccountNotActivatedException extends MokBaseException {

    public AccountNotActivatedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public static AccountNotActivatedException withDefaultMsg() {
        return new AccountNotActivatedException(ACCOUNT_NOT_ENABLED_EXCEPTION);
    }

}
