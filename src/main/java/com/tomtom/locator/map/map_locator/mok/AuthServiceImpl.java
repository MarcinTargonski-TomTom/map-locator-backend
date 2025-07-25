package com.tomtom.locator.map.map_locator.mok;

import com.tomtom.locator.map.map_locator.mok.exception.AccountNotEnabledException;
import com.tomtom.locator.map.map_locator.mok.exception.InvalidCredentialsException;
import com.tomtom.locator.map.map_locator.mok.model.Account;
import com.tomtom.locator.map.map_locator.mok.model.Credentials;
import com.tomtom.locator.map.map_locator.mok.model.Tokens;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    AuthServiceImpl(@NonNull AccountRepository accountRepository, @NonNull PasswordEncoder passwordEncoder, @NonNull JwtHelper jwtHelper) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtHelper = jwtHelper;
    }

    @Override
    public Tokens authenticate(@NonNull Credentials credentials) {
        Account account = accountRepository.findByLogin(credentials.login())
                .orElseThrow(InvalidCredentialsException::withDefaultMsg);

        if (!account.isEnabled()) {
            throw AccountNotEnabledException.withDefaultMsg();
        }

        if (!passwordEncoder.matches(credentials.password(), account.getPassword())) {
            throw InvalidCredentialsException.withDefaultMsg();
        }

        String authToken = jwtHelper.generateAuthTokenForAnAccount(account);
        return new Tokens(authToken);
    }
}
