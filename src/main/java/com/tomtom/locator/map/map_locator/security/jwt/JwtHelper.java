package com.tomtom.locator.map.map_locator.security.jwt;

import com.tomtom.locator.map.map_locator.model.Account;
import lombok.NonNull;

public interface JwtHelper {

    String generateAuthTokenForAnAccount(Account account);

    String extractSubject(@NonNull String token);
}
