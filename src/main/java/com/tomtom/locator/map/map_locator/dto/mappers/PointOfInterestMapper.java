package com.tomtom.locator.map.map_locator.dto.mappers;

import com.tomtom.locator.map.map_locator.dto.PointOfInterestDTO;
import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PointMapper.class)
public interface PointOfInterestMapper {


    PointOfInterest toModel(PointOfInterestDTO pointOfInterestDTO);

    PointOfInterestDTO toDto(PointOfInterest pointOfInterest);
}
