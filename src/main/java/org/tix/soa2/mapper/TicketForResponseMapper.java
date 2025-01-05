package org.tix.soa2.mapper;

import com.example.model.TicketForResponse;
import org.springframework.stereotype.Component;
import org.tix.soa2.model.TicketEntity;


@Component
public class TicketForResponseMapper {
    private final UtilMapper utilMapper;

    public TicketForResponseMapper(UtilMapper utilMapper) {
        this.utilMapper = utilMapper;
    }


    public TicketForResponse toDTO(TicketEntity ticketEntity){
        TicketForResponse ticketForResponse = new TicketForResponse();
        ticketForResponse.setId(ticketEntity.getId());
        ticketForResponse.setComment(ticketEntity.getComment());
        ticketForResponse.setCoordinates(utilMapper.mapCoordinatesEntityToCoordinates(ticketEntity.getCoordinates()));
        ticketForResponse.setPerson(utilMapper.mapPersonEntityToPerson(ticketEntity.getPerson()));
        ticketForResponse.setName(ticketEntity.getName());
        ticketForResponse.setType(ticketEntity.getType());
        ticketForResponse.setPrice(ticketEntity.getPrice());
        ticketForResponse.setCreationDate(ticketEntity.getCreationDate());
        return ticketForResponse;
    }


}
