package com.tomtom.locator.map.map_locator.mok;

import com.tomtom.locator.map.map_locator.mok.dto.NewAccountDto;
import com.tomtom.locator.map.map_locator.mok.model.Account;
import com.tomtom.locator.map.map_locator.mok.model.Credentials;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    AccountServiceImpl(@NonNull AccountRepository accountRepository, @NonNull PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Account create(NewAccountDto dto) {
        Credentials credentials = new Credentials(dto.login(), passwordEncoder.encode(dto.password()));
        Account account = Account.withEmailAndCredentials(dto.email(), credentials);

        return accountRepository.saveAndFlush(account);
    }
}
