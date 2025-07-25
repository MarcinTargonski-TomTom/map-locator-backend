package com.tomtom.locator.map.map_locator.mok.model;

import lombok.NonNull;

public record Credentials(@NonNull String login, @NonNull String password) {
}
