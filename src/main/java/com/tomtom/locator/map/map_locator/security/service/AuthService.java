package com.tomtom.locator.map.map_locator.security.service;

import com.tomtom.locator.map.map_locator.model.Account;
import com.tomtom.locator.map.map_locator.security.model.Credentials;
import com.tomtom.locator.map.map_locator.security.model.Tokens;
import lombok.NonNull;

public interface AuthService {

    Tokens authenticate(@NonNull Credentials credentials);

    Tokens extendSession(@NonNull String login, @NonNull String refreshToken);

    Account findByLogin(@NonNull String login);
}
