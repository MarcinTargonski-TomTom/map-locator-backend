package com.tomtom.locator.map.map_locator.mom.repository;

import com.tomtom.locator.map.map_locator.model.LocationMatch;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
public interface LocationMatchRepository extends Repository<LocationMatch, UUID> {

    LocationMatch save(LocationMatch locationMatch);

    LocationMatch saveAndFlush(LocationMatch locationMatch);

    List<LocationMatch> saveAllAndFlush(Iterable<LocationMatch> locationMatches);

    @Query("SELECT lm FROM LocationMatch lm WHERE lm.account.login = :login")
    List<LocationMatch> findAllByAccount(@Param("login") String login);

}
