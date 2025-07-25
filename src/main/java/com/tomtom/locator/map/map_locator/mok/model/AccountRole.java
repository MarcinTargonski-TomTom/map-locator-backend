package com.tomtom.locator.map.map_locator.mok.model;

import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;

public enum AccountRole implements GrantedAuthority {
    TENANT("Tenant");

    private final String authority;

    AccountRole(@NonNull String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}