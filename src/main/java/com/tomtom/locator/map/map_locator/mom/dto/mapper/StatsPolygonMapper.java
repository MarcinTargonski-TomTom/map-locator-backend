package com.tomtom.locator.map.map_locator.mom.dto.mapper;

import com.tomtom.locator.map.map_locator.model.StatsPolygon;
import com.tomtom.locator.map.map_locator.mom.dto.StatsPolygonDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = PointMapper.class)
public interface StatsPolygonMapper {

    StatsPolygonDTO toDTO(StatsPolygon statsPolygon);

    StatsPolygon toModel(StatsPolygonDTO statsPolygonDTO);

    List<StatsPolygonDTO> toDTOList(List<StatsPolygon> statsPolygons);

    List<StatsPolygon> toModelList(List<StatsPolygonDTO> statsPolygonDTOs);
}
