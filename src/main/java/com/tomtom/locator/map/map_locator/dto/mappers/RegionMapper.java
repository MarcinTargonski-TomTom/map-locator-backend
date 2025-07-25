package com.tomtom.locator.map.map_locator.dto.mappers;

import com.tomtom.locator.map.map_locator.dto.RegionDTO;
import com.tomtom.locator.map.map_locator.model.ReachableRange;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = PointMapper.class)
public interface RegionMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    RegionDTO toDTO(ReachableRange region);

    ReachableRange toModel(RegionDTO regionDTO);

}
