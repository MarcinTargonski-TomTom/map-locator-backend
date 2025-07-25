package com.tomtom.locator.map.map_locator.dto.mappers;

import com.tomtom.locator.map.map_locator.dto.PointDTO;
import com.tomtom.locator.map.map_locator.model.Point;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PointMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    PointDTO doDTO(Point point);

    Point toModel(PointDTO pointDTO);

}
