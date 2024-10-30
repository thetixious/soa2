package org.tix.soa2.mapper;

import com.example.model.Person;
import org.mapstruct.*;
import org.tix.soa2.model.PersonEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PersonEntityMapper {
    org.tix.soa2.model.PersonEntity toEntity(Person person);

    Person toDto(org.tix.soa2.model.PersonEntity personEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PersonEntity partialUpdate(Person person, @MappingTarget PersonEntity personEntity);
}