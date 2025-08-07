package com.tomtom.locator.map.map_locator.security.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.concurrent.TimeUnit;

public record JwtProperties(
        @NotNull @Positive Integer timeoutInMinutes,
        @NotBlank String key
) {

    public long timeoutInMillis() {
        return TimeUnit.MINUTES.toMillis(timeoutInMinutes);
    }
}
