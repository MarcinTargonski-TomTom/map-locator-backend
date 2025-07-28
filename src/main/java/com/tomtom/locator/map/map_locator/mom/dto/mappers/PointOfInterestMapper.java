package com.tomtom.locator.map.map_locator.mom.dto.mappers;

import com.tomtom.locator.map.map_locator.mom.dto.PointOfInterestDTO;
import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PointMapper.class})
public interface PointOfInterestMapper {


    PointOfInterest toModel(PointOfInterestDTO pointOfInterestDTO);

    PointOfInterestDTO toDto(PointOfInterest pointOfInterest);

    List<PointOfInterestDTO> toDto(List<PointOfInterest> pointsOfInterest);

    List<PointOfInterest> toModel(List<PointOfInterestDTO> pointsOfInterest);

}
