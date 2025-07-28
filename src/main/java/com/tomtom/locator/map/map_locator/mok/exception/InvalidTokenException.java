package com.tomtom.locator.map.map_locator.mok.exception;

import static com.tomtom.locator.map.map_locator.mok.exception.MokExceptionMessage.INVALID_TOKEN_EXCEPTION;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }

  public InvalidTokenException(String message, Throwable cause) {
    super(message, cause);
  }

  public static InvalidTokenException withDefaultMsgAndCause(Exception cause) {
        return new InvalidTokenException(INVALID_TOKEN_EXCEPTION, cause);
    }
}
