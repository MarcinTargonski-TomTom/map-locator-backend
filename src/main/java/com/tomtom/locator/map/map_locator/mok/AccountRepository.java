package com.tomtom.locator.map.map_locator.mok;

import com.tomtom.locator.map.map_locator.mok.model.Account;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(propagation = Propagation.MANDATORY)
interface AccountRepository extends Repository<Account, String> {

    Optional<Account> findByLogin(String login);

    Optional<Account> findByEmail(String email);

    Account saveAndFlush(Account account);
}
