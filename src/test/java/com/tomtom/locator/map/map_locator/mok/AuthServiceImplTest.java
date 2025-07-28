package com.tomtom.locator.map.map_locator.mok;

import com.tomtom.locator.map.map_locator.mok.exception.AccountNotActiveException;
import com.tomtom.locator.map.map_locator.mok.exception.InvalidCredentialsException;
import com.tomtom.locator.map.map_locator.mok.helper.JwtHelper;
import com.tomtom.locator.map.map_locator.mok.model.Account;
import com.tomtom.locator.map.map_locator.mok.model.Credentials;
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
    private AccountRepository accountRepository;

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
        given(accountRepository.findByLogin(anyString()))
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

        given(accountRepository.findByLogin(anyString()))
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

        given(accountRepository.findByLogin(anyString()))
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

        given(accountRepository.findByLogin(anyString()))
                .willReturn(Optional.of(givenAccount));

        given(passwordEncoder.matches(anyString(), anyString()))
                .willReturn(true);

        given(jwtHelper.generateAuthTokenForAnAccount(any()))
                .willReturn("token");

        // When
        var result = underTest.authenticate(CORRECT_CREDENTIALS);

        // Then
        assertThat(result)
                .isNotNull();
    }
}