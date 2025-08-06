package com.tomtom.locator.map.map_locator.security.model;

import lombok.NonNull;

public record Tokens(@NonNull String auth, @NonNull String refresh) {
}
