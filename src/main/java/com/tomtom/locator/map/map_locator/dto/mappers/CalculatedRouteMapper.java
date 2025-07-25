package com.tomtom.locator.map.map_locator.dto.mappers;

import com.tomtom.locator.map.map_locator.dto.CalculatedRouteDTO;
import com.tomtom.locator.map.map_locator.model.CalculatedRoutes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = RegionMapper.class)
public interface CalculatedRouteMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    CalculatedRouteDTO toDTO(CalculatedRoutes calculatedRoutes);

    CalculatedRoutes toModel(CalculatedRouteDTO calculatedRouteDTO);

}
