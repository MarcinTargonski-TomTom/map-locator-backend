package com.tomtom.locator.map.map_locator.mom.dto.mapper;

import com.tomtom.locator.map.map_locator.model.Point;
import com.tomtom.locator.map.map_locator.mom.dto.PointDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PointMapper {

    PointDTO doDTO(Point point);

    Point toModel(PointDTO pointDTO);

}
