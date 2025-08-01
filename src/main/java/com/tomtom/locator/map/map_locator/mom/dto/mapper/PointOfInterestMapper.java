package com.tomtom.locator.map.map_locator.mom.dto.mapper;

import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import com.tomtom.locator.map.map_locator.model.SearchApiResult;
import com.tomtom.locator.map.map_locator.mom.dto.PointOfInterestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PointMapper.class})
public interface PointOfInterestMapper {


    PointOfInterest toModel(PointOfInterestDTO pointOfInterestDTO);

    PointOfInterestDTO toDto(PointOfInterest pointOfInterest);

    List<PointOfInterestDTO> toDto(List<PointOfInterest> pointsOfInterest);

    List<PointOfInterest> toModel(List<PointOfInterestDTO> pointsOfInterest);

    @Mapping(target = "center", expression = "java(new com.tomtom.locator.map.map_locator.model.Point(result.getPosition().getLat(), result.getPosition().getLon()))")
    @Mapping(target = "name", expression = "java(result.getPoi() != null ? result.getPoi().getName() : \"\")")
    @Mapping(target = "budgetType", source = "original.budgetType")
    @Mapping(target = "value", source = "original.value")
    @Mapping(target = "travelMode", source = "original.travelMode")
    PointOfInterest fromSearchApiResult(SearchApiResult result, PointOfInterest original);
}
