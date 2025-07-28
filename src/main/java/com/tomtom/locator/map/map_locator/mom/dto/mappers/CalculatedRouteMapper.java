package com.tomtom.locator.map.map_locator.mom.dto.mappers;

import com.tomtom.locator.map.map_locator.mom.dto.CalculatedRouteDTO;
import com.tomtom.locator.map.map_locator.model.CalculatedRoute;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = RegionMapper.class)
public interface CalculatedRouteMapper {


    CalculatedRouteDTO toDTO(CalculatedRoute calculatedRoute);

    CalculatedRoute toModel(CalculatedRouteDTO calculatedRouteDTO);

}
