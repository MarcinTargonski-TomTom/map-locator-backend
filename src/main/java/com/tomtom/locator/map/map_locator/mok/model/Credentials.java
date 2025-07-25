package com.tomtom.locator.map.map_locator.mok.model;

import jakarta.validation.constraints.NotBlank;

public record Credentials(@NotBlank String login, @NotBlank String password) {
}
