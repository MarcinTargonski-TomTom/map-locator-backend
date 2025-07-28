package com.tomtom.locator.map.map_locator.exception;

import org.springframework.http.HttpStatus;

import static com.tomtom.locator.map.map_locator.exception.ExceptionMessage.ACCOUNT_NOT_ACTIVE_EXCEPTION;

public class AccountNotActiveException extends AppBaseException {

    public AccountNotActiveException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public static AccountNotActiveException withDefaultMsg() {
        return new AccountNotActiveException(ACCOUNT_NOT_ACTIVE_EXCEPTION);
    }

}
