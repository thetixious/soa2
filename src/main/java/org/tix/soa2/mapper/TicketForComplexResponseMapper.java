package org.tix.soa2.mapper;

import com.example.model.TicketForComplexResponse;
import org.springframework.stereotype.Component;
import org.tix.soa2.model.TicketEntity;

import java.time.LocalDate;

@Component
public class TicketForComplexResponseMapper {
    private final UtilMapper utilMapper;


    public TicketForComplexResponseMapper(UtilMapper utilMapper) {
        this.utilMapper = utilMapper;
    }

    public TicketForComplexResponse toDTO(TicketEntity ticketEntity){
        TicketForComplexResponse ticketForComplexResponse = new TicketForComplexResponse();
        ticketForComplexResponse.setId(ticketEntity.getId());
        ticketForComplexResponse.setName(ticketEntity.getName());
        ticketForComplexResponse.setCreationDate(LocalDate.from(ticketEntity.getCreationDate()));
        ticketForComplexResponse.setComment(ticketEntity.getComment());
        ticketForComplexResponse.setCoordinates(utilMapper.mapCoordinatesEntityToCoordinates(ticketEntity.getCoordinates()));
        ticketForComplexResponse.setPerson(utilMapper.mapPersonEntityToPerson(ticketEntity.getPerson()));
        ticketForComplexResponse.setType(ticketEntity.getType());
        ticketForComplexResponse.setPrice(ticketEntity.getPrice());
        return ticketForComplexResponse;
    }
}
