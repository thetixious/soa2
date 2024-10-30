package org.tix.soa2.mapper;

import com.example.model.Ticket;
import org.mapstruct.*;
import org.tix.soa2.model.TicketEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TicketEntityMapper {
    org.tix.soa2.model.TicketEntity toEntity(Ticket ticket);

    Ticket toDto(org.tix.soa2.model.TicketEntity ticketEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TicketEntity partialUpdate(Ticket ticket, @MappingTarget TicketEntity ticketEntity);
}