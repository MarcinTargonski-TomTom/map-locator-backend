package com.tomtom.locator.map.map_locator.security.service;

import com.tomtom.locator.map.map_locator.logger.MethodCallLogged;
import com.tomtom.locator.map.map_locator.model.Account;
import com.tomtom.locator.map.map_locator.exception.AccountNotActiveException;
import com.tomtom.locator.map.map_locator.exception.InvalidCredentialsException;
import com.tomtom.locator.map.map_locator.security.repository.AuthRepository;
import com.tomtom.locator.map.map_locator.security.jwt.JwtHelper;
import com.tomtom.locator.map.map_locator.security.model.Credentials;
import com.tomtom.locator.map.map_locator.security.model.Tokens;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;


@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor
@MethodCallLogged
class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    @Override
    public Tokens authenticate(@NonNull Credentials credentials) {
        Account account = authRepository.findByLogin(credentials.login())
                .orElseThrow(InvalidCredentialsException::withDefaultMsg);

        if (!account.isEnabled()) {
            throw AccountNotActiveException.withDefaultMsg();
        }

        if (!passwordEncoder.matches(credentials.password(), account.getPassword())) {
            throw InvalidCredentialsException.withDefaultMsg();
        }

        String authToken = jwtHelper.generateAuthTokenForAnAccount(account);
        return new Tokens(authToken);
    }

    @Override
    public Account findByLogin(@NonNull String login) {
        return authRepository.findByLogin(login)
                .orElseThrow(() -> new NoSuchElementException("Account with given login from subject=%s can't be found".formatted(login)));
    }
}
