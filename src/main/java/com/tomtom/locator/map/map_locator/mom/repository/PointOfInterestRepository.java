package com.tomtom.locator.map.map_locator.mom.repository;

import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
public interface PointOfInterestRepository extends Repository<PointOfInterest, UUID> {

    PointOfInterest save(PointOfInterest pointOfInterest);
}
