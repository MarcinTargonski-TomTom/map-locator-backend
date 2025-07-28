package com.tomtom.locator.map.map_locator.dto.mappers;

import com.tomtom.locator.map.map_locator.dto.PointDTO;
import com.tomtom.locator.map.map_locator.model.Point;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PointMapper {
    
    PointDTO doDTO(Point point);

    Point toModel(PointDTO pointDTO);

}
