package com.tomtom.locator.map.map_locator.mom.service.matcher;

import com.tomtom.locator.map.map_locator.model.Account;
import com.tomtom.locator.map.map_locator.model.LocationMatch;
import com.tomtom.locator.map.map_locator.mok.repository.AccountRepository;
import com.tomtom.locator.map.map_locator.mom.repository.LocationMatchRepository;
import com.tomtom.locator.map.map_locator.mom.repository.PointOfInterestRepository;
import com.tomtom.locator.map.map_locator.security.model.Credentials;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class LocationMatchServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private LocationMatchRepository locationMatchRepository;

    @Mock
    private PointOfInterestRepository pointOfInterestRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LocationMatchServiceImpl underTest;

    @DisplayName("Should add location match to account when account exists")
    @Test
    void addToAccount() throws ExecutionException, InterruptedException {
        // Given
        var givenLocationMatch = mock(LocationMatch.class);
        var givenLocationMatches = List.of(givenLocationMatch);
        var givenAccount = Account.withEmailAndCredentials("email", new Credentials("login", "password"));

        given(accountRepository.findByLogin(any()))
                .willReturn(Optional.of(givenAccount));

        // When
        var result = underTest.addToAccount(givenAccount.getUsername(), givenLocationMatches).get();

        // Then
        assertThat(result)
                .allMatch(locationMatch -> locationMatch.getAccount().equals(givenAccount));
    }

    @DisplayName("Should throw when account does not exist")
    @Test
    void shouldFailToAddToAccount() {
        // Given
        var givenLocationMatch = mock(LocationMatch.class);
        var givenLocationMatches = List.of(givenLocationMatch);
        given(accountRepository.findByLogin(any()))
                .willReturn(Optional.empty());

        // When
        var result = catchException(() -> underTest.addToAccount("incorrect", givenLocationMatches));

        // Then
        assertThat(result)
                .isNotNull();
    }
}