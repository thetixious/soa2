package org.tix.soa2.mapper;

import com.example.model.TicketForUserDTO;
import org.springframework.stereotype.Component;
import org.tix.soa2.model.PersonEntity;
import org.tix.soa2.model.TicketEntity;

@Component
public class TicketForUserDTOMapper {
    private final UtilMapper utilMapper;

    public TicketForUserDTOMapper(UtilMapper utilMapper) {
        this.utilMapper = utilMapper;
    }
    public TicketEntity toEntity(TicketForUserDTO ticket, PersonEntity person){
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setName(ticket.getName());
        ticketEntity.setPrice(ticket.getPrice());
        ticketEntity.setType(ticket.getType());
        ticketEntity.setComment(ticket.getComment());
        ticketEntity.setCoordinates(utilMapper.mapCoordinatesToCoordinatesEntity(ticket.getCoordinates()));
        ticketEntity.setPerson(person);
        return ticketEntity;
    }
}
