package org.tix.soa2.model.DTO;

import com.example.model.Coordinates;
import com.example.model.TicketType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Data
@Getter
@Setter
public class TicketForUserDTO {

    private String name;
    private Coordinates coordinates;
    private ZonedDateTime creationDate;
    private Double price;
    private String comment;
    private TicketType ticketType;
    private Long personId;



}
