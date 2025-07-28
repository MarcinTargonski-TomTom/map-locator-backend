package com.tomtom.locator.map.map_locator.mok.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

@ConfigurationProperties(prefix = "app.mok.security.auth-token")
public record JwtProperties(@NotNull @Positive Integer timeoutInMinutes, @NotBlank String key) {

    public long timeoutInMillis() {
        return TimeUnit.MINUTES.toMillis(timeoutInMinutes);
    }
}
