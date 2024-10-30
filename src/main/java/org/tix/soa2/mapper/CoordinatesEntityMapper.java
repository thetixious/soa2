package org.tix.soa2.mapper;

import com.example.model.Coordinates;
import org.mapstruct.*;
import org.tix.soa2.model.CoordinatesEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CoordinatesEntityMapper {
    org.tix.soa2.model.CoordinatesEntity toEntity(Coordinates coordinates);

    Coordinates toDto(org.tix.soa2.model.CoordinatesEntity coordinatesEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CoordinatesEntity partialUpdate(Coordinates coordinates, @MappingTarget CoordinatesEntity coordinatesEntity);
}