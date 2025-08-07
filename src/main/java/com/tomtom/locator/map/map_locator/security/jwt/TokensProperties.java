package com.tomtom.locator.map.map_locator.security.jwt;

import jakarta.validation.Valid;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.mok.security")
record TokensProperties(
        @Valid JwtProperties authToken,
        @Valid JwtProperties refreshToken
) {

}
