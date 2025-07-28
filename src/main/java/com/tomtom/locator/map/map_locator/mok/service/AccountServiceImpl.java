package com.tomtom.locator.map.map_locator.mok.service;

import com.tomtom.locator.map.map_locator.logger.MethodCallLogged;
import com.tomtom.locator.map.map_locator.model.Account;
import com.tomtom.locator.map.map_locator.mok.dto.NewAccountDto;
import com.tomtom.locator.map.map_locator.mok.repository.AccountRepository;
import com.tomtom.locator.map.map_locator.security.model.Credentials;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor
@MethodCallLogged
class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Account create(@NonNull NewAccountDto dto) {
        Credentials credentials = new Credentials(dto.login(), passwordEncoder.encode(dto.password()));
        Account account = Account.withEmailAndCredentials(dto.email(), credentials);
        account.activate();

        return accountRepository.saveAndFlush(account);
    }
}
