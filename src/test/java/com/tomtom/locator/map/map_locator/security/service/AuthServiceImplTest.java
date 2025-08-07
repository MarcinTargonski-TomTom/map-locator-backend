package com.tomtom.locator.map.map_locator.security.service;

import com.tomtom.locator.map.map_locator.exception.AccountNotActiveException;
import com.tomtom.locator.map.map_locator.exception.InvalidCredentialsException;
import com.tomtom.locator.map.map_locator.model.Account;
import com.tomtom.locator.map.map_locator.security.jwt.JwtHelper;
import com.tomtom.locator.map.map_locator.security.model.Credentials;
import com.tomtom.locator.map.map_locator.security.model.Tokens;
import com.tomtom.locator.map.map_locator.security.repository.AuthRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtHelper jwtHelper;

    @InjectMocks
    private AuthServiceImpl underTest;

    private final Credentials CORRECT_CREDENTIALS = new Credentials("john123", "supersecret");
    private final Credentials INCORRECT_CREDENTIALS = new Credentials("john123", "suadfapersecret");

    @Test
    @DisplayName("Should throw if account can't be found")
    void authenticateShouldThrow() {
        // Given
        given(authRepository.findByLogin(anyString()))
                .willReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.authenticate(CORRECT_CREDENTIALS))
                .isNotNull()
                .isExactlyInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    @DisplayName("Should throw if account is not enabled")
    void authenticateShouldThrow2() {
        // Given
        var givenAccount = Account.withEmailAndCredentials("example@example.com", CORRECT_CREDENTIALS);
        givenAccount.lock();

        given(authRepository.findByLogin(anyString()))
                .willReturn(Optional.of(givenAccount));

        // When
        // Then
        assertThatThrownBy(() -> underTest.authenticate(CORRECT_CREDENTIALS))
                .isNotNull()
                .isExactlyInstanceOf(AccountNotActiveException.class);
    }

    @Test
    @DisplayName("Should throw if password is incorrect")
    void authenticateShouldThrow3() {
        // Given
        var givenAccount = Account.withEmailAndCredentials("example@example.com", CORRECT_CREDENTIALS);
        givenAccount.activate();

        given(authRepository.findByLogin(anyString()))
                .willReturn(Optional.of(givenAccount));

        // When
        // Then
        assertThatThrownBy(() -> underTest.authenticate(INCORRECT_CREDENTIALS))
                .isNotNull()
                .isExactlyInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    @DisplayName("Should generate auth token")
    void authenticateSuccess() {
        // Given
        var givenAccount = Account.withEmailAndCredentials("example@example.com", CORRECT_CREDENTIALS);
        givenAccount.activate();

        given(authRepository.findByLogin(anyString()))
                .willReturn(Optional.of(givenAccount));

        given(passwordEncoder.matches(anyString(), anyString()))
                .willReturn(true);

        given(jwtHelper.generateAuthTokenForAnAccount(any()))
                .willReturn("token");

        given(jwtHelper.generateRefreshTokenForAnAccount(any()))
                .willReturn("token");

        // When
        var result = underTest.authenticate(CORRECT_CREDENTIALS);

        // Then
        assertThat(result)
                .isNotNull();
    }

    @Test
    @DisplayName("Should throw if login from refresh token does not match login")
    void extendSessionShouldThrow() {
        // Given
        var givenAccount = Account.withEmailAndCredentials("example@example.com", CORRECT_CREDENTIALS);
        givenAccount.activate();

        given(jwtHelper.extractSubject(anyString()))
                .willReturn("john123");

        // When + Then
        assertThatThrownBy(() -> underTest.extendSession("login", "refreshToken"))
                .isNotNull();
    }

    @Test
    @DisplayName("Should throw if account can't be found")
    void extendSessionShouldThrow2() {
        // Given
        given(jwtHelper.extractSubject(anyString()))
                .willReturn("john123");

        given(authRepository.findByLogin(anyString()))
                .willReturn(Optional.empty());

        // When + Then
        assertThatThrownBy(() -> underTest.extendSession("john123", "refreshToken"))
                .isNotNull();
    }

    @Test
    @DisplayName("Should return new auth token if everything is correct")
    void extendSessionSuccess() {
        // Given
        var givenAccount = Account.withEmailAndCredentials("example@example.com", CORRECT_CREDENTIALS);
        givenAccount.activate();

        given(jwtHelper.extractSubject(anyString()))
                .willReturn(CORRECT_CREDENTIALS.login());

        given(authRepository.findByLogin(anyString()))
                .willReturn(Optional.of(givenAccount));

        given(jwtHelper.generateAuthTokenForAnAccount(any()))
                .willReturn("newAuthToken");

        // When
        var result = underTest.extendSession(CORRECT_CREDENTIALS.login(), "refreshToken");

        // Then
        assertThat(result)
                .isNotNull()
                .extracting(Tokens::auth)
                .isEqualTo("newAuthToken");
    }
}