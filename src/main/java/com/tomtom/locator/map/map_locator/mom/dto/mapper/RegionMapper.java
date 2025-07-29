package com.tomtom.locator.map.map_locator.mom.dto.mapper;

import com.tomtom.locator.map.map_locator.model.Region;
import com.tomtom.locator.map.map_locator.mom.dto.RegionDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = PointMapper.class)
public interface RegionMapper {

    RegionDTO toDTO(Region region);

    Region toModel(RegionDTO regionDTO);

    List<RegionDTO> toDTO(List<Region> regionList);

    List<Region> toModel(List<RegionDTO> regionList);

}
