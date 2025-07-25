package com.tomtom.locator.map.map_locator.mok;

import com.tomtom.locator.map.map_locator.mok.model.Account;
import lombok.NonNull;

import java.util.Map;

interface JwtHelper {

    String generateAuthTokenForAnAccount(Account account);

    Map<String, Object> validateAndExtract(@NonNull String token);
}
