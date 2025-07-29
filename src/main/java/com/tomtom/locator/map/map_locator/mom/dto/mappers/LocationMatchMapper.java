package com.tomtom.locator.map.map_locator.mom.dto.mappers;

import com.tomtom.locator.map.map_locator.model.LocationMatch;
import com.tomtom.locator.map.map_locator.mom.dto.LocationMatchDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = RegionMapper.class)
public interface LocationMatchMapper {

    LocationMatchDTO toDTO(LocationMatch locationMatch);

    LocationMatch toModel(LocationMatchDTO locationMatchDTO);

    List<LocationMatchDTO> toDTO(List<LocationMatch> list);

    List<LocationMatch> toModel(List<LocationMatchDTO> list);

}
