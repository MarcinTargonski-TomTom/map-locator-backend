package com.tomtom.locator.map.map_locator.mok;

import com.tomtom.locator.map.map_locator.mok.model.Credentials;
import com.tomtom.locator.map.map_locator.mok.model.Tokens;
import lombok.NonNull;

interface AuthService {

    Tokens authenticate(@NonNull Credentials credentials);
}
