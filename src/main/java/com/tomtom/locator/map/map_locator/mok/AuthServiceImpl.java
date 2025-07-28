package com.tomtom.locator.map.map_locator.mok;

import com.tomtom.locator.map.map_locator.mok.exception.AccountNotActiveException;
import com.tomtom.locator.map.map_locator.mok.exception.InvalidCredentialsException;
import com.tomtom.locator.map.map_locator.mok.helper.JwtHelper;
import com.tomtom.locator.map.map_locator.mok.model.Account;
import com.tomtom.locator.map.map_locator.mok.model.Credentials;
import com.tomtom.locator.map.map_locator.mok.model.Tokens;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor
class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    @Override
    public Tokens authenticate(@NonNull Credentials credentials) {
        Account account = accountRepository.findByLogin(credentials.login())
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
}
