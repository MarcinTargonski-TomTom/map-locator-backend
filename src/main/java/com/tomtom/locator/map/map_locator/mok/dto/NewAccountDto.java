package com.tomtom.locator.map.map_locator.mok.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NewAccountDto(
        @NotBlank
        String login,
        @Email
        String email,
        @NotBlank
        String password
) {
}
