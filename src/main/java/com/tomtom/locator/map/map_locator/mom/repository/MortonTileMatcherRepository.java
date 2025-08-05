package com.tomtom.locator.map.map_locator.mom.repository;

import com.tomtom.locator.map.map_locator.model.MortonTileMatcher;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(propagation = Propagation.MANDATORY)
public interface MortonTileMatcherRepository extends Repository<MortonTileMatcher, Long> {

    void saveAllAndFlush(Iterable<MortonTileMatcher> mortonTileMatchers);

    Optional<MortonTileMatcher> findByMortonCode(long mortonCode);

    List<MortonTileMatcher> findAll();
}
