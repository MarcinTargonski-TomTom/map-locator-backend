package com.tomtom.locator.map.map_locator.security.model;

import lombok.NonNull;

public record Credentials(@NonNull String login, @NonNull String password) {
}
