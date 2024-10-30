package org.tix.soa2.mapper;


import com.example.model.Ticket;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.stereotype.Component;

import org.tix.soa2.model.TicketEntity;


@Component
public class TicketMapper {

    private final UtilMapper utilMapper;


    public TicketMapper(UtilMapper utilMapper) {

        this.utilMapper = utilMapper;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JsonNullableModule());

    }

    public TicketEntity toEntity(Ticket ticket){
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setName(ticket.getName());
        ticketEntity.setPrice(ticket.getPrice());
        ticketEntity.setType(ticket.getType());
        ticketEntity.setComment(ticket.getComment());
        ticketEntity.setCoordinates(utilMapper.mapCoordinatesToCoordinatesEntity(ticket.getCoordinates()));
        ticketEntity.setPerson(utilMapper.mapPersonToPersonEntity(ticket.getPerson()));
        return ticketEntity;
    }

}

