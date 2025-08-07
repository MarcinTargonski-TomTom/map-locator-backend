package com.tomtom.locator.map.map_locator.mom.dto.mapper;

import com.tomtom.locator.map.map_locator.model.Stat;
import com.tomtom.locator.map.map_locator.mom.dto.StatDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatMapper {

    StatDTO toDTO(Stat stat);

    Stat toModel(StatDTO statDTO);

    List<StatDTO> toDTOList(List<Stat> stats);

    List<Stat> toModelList(List<StatDTO> statDTOs);

}
