package com.tomtom.locator.map.map_locator.mom.dto.mappers;

import com.tomtom.locator.map.map_locator.mom.dto.PointDTO;
import com.tomtom.locator.map.map_locator.model.Point;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PointMapper {
    
    PointDTO doDTO(Point point);

    Point toModel(PointDTO pointDTO);

}
