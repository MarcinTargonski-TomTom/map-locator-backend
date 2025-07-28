package com.tomtom.locator.map.map_locator.security.repository;

import com.tomtom.locator.map.map_locator.model.Account;
import lombok.NonNull;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
public interface AuthRepository extends Repository<Account, UUID> {

    Optional<Account> findByLogin(@NonNull String login);
}
