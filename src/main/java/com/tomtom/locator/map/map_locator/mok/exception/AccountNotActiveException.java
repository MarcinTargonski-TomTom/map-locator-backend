package com.tomtom.locator.map.map_locator.mok.exception;

import org.springframework.http.HttpStatus;

import static com.tomtom.locator.map.map_locator.mok.exception.MokExceptionMessage.ACCOUNT_NOT_ACTIVE_EXCEPTION;

public class AccountNotActiveException extends MokBaseException {

    public AccountNotActiveException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public static AccountNotActiveException withDefaultMsg() {
        return new AccountNotActiveException(ACCOUNT_NOT_ACTIVE_EXCEPTION);
    }

}
