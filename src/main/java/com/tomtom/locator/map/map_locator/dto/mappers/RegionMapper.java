package com.tomtom.locator.map.map_locator.dto.mappers;

import com.tomtom.locator.map.map_locator.dto.RegionDTO;
import com.tomtom.locator.map.map_locator.model.Region;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PointMapper.class)
public interface RegionMapper {

    RegionDTO toDTO(Region region);

    Region toModel(RegionDTO regionDTO);

}
