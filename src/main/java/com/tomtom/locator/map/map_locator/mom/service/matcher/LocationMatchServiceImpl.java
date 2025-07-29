package com.tomtom.locator.map.map_locator.mom.service.matcher;

import com.tomtom.locator.map.map_locator.model.LocationMatch;
import com.tomtom.locator.map.map_locator.mok.repository.AccountRepository;
import com.tomtom.locator.map.map_locator.mom.repository.LocationMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor
class LocationMatchServiceImpl implements LocationMatchService {

    private final AccountRepository accountRepository;
    private final LocationMatchRepository locationMatchRepository;

    @Override
    public LocationMatch addToAccount(LocationMatch locationMatch) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String login = authentication.getName();

        accountRepository.findByLogin(login)
                .ifPresent(account -> {
                    account.addLocationMatch(locationMatch);
                    locationMatchRepository.save(locationMatch);
                });

        return locationMatch;
    }
}
