package com.tomtom.locator.map.map_locator.mok.repository;

import com.tomtom.locator.map.map_locator.model.Account;
import lombok.NonNull;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
public interface AccountRepository extends Repository<Account, UUID> {

    Account saveAndFlush(@NonNull Account account);

    Optional<Account> findByLogin(@NonNull String login);
}
