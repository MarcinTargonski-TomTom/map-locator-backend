package com.tomtom.locator.map.map_locator.mok.helper;

import com.tomtom.locator.map.map_locator.mok.model.Account;
import lombok.NonNull;

public interface JwtHelper {

    String generateAuthTokenForAnAccount(Account account);

    String extractSubject(@NonNull String token);
}
