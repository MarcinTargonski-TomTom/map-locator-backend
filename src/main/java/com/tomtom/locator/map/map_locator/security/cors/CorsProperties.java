package com.tomtom.locator.map.map_locator.security.cors;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@ConfigurationProperties("app.mok.security.cors")
public record CorsProperties(
        @NotEmpty
        List<String> allowedOrigins
) {}