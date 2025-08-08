package com.tomtom.locator.map.map_locator.model;

import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;

public enum AccountRole implements GrantedAuthority {
    TENANT("ROLE_TENANT"),
    PREMIUM("ROLE_PREMIUM");

    private final String authority;

    AccountRole(@NonNull String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
