package com.tomtom.locator.map.map_locator.mom.dto.mapper;

import com.tomtom.locator.map.map_locator.model.CalculatedRoute;
import com.tomtom.locator.map.map_locator.mom.dto.CalculatedRouteDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = RegionMapper.class)
public interface CalculatedRouteMapper {


    CalculatedRouteDTO toDTO(CalculatedRoute calculatedRoute);

    CalculatedRoute toModel(CalculatedRouteDTO calculatedRouteDTO);

}
