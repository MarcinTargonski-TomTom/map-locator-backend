package com.tomtom.locator.map.map_locator.model;

import com.tomtom.locator.map.map_locator.security.model.Credentials;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    private final Credentials CORRECT_CREDENTIALS = new Credentials("login123", "adminadmin");
    private final String EMAIL = "example@example.com";
    private final Account underTest = Account.withEmailAndCredentials(EMAIL, CORRECT_CREDENTIALS);

    @Test
    @DisplayName("Should have given email and credentials, and default state")
    void withEmailAndCredentials() {
        // When
        var result = Account.withEmailAndCredentials(EMAIL, CORRECT_CREDENTIALS);

        // Then
        assertThat(result.getEmail())
                .isEqualTo(EMAIL);

        assertThat(result.getUsername())
                .isEqualTo(CORRECT_CREDENTIALS.login());

        assertThat(result.getPassword())
                .isEqualTo(CORRECT_CREDENTIALS.password());
    }

    @Test
    @DisplayName("Should lock active account")
    void lock() {
        // Given
        underTest.activate();

        // When
        underTest.lock();

        // Then
        assertThat(underTest.isEnabled())
                .isFalse();
    }

    @Test
    @DisplayName("Should activate locked account")
    void activate() {
        // Given
        underTest.lock();

        // When
        underTest.activate();

        // Then
        assertThat(underTest.isEnabled())
                .isTrue();
    }

    @Test
    void archive() {
        // Given
        underTest.archive();

        // When
        underTest.archive();

        // Then
        assertThat(underTest.isEnabled())
                .isFalse();
    }
}