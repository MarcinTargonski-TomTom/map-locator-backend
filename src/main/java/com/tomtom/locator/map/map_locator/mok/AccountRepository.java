package com.tomtom.locator.map.map_locator.mok;

import com.tomtom.locator.map.map_locator.mok.model.Account;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
public interface AccountRepository extends Repository<Account, UUID> {

    Optional<Account> findByLogin(String login);

    Optional<Account> findByEmail(String email);

    Account saveAndFlush(Account account);
}
