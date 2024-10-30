package org.tix.soa2.mapper;

import com.example.model.Coordinates;
import com.example.model.Person;
import com.example.model.Ticket;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.stereotype.Component;
import org.tix.soa2.model.CoordinatesEntity;
import org.tix.soa2.model.PersonEntity;
import org.tix.soa2.model.TicketEntity;
import org.tix.soa2.repo.CoordinatesRepo;
import org.tix.soa2.repo.PersonRepository;

@Component
public class TicketMapper {
    private final CoordinatesRepo coordinatesRepo;
    private final PersonRepository personRepository;


    public TicketMapper(CoordinatesRepo coordinatesRepo, PersonRepository personRepository) {
        this.coordinatesRepo = coordinatesRepo;
        this.personRepository = personRepository;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JsonNullableModule());

    }

    public TicketEntity toEntity(Ticket ticket){
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setName(ticket.getName());
        ticketEntity.setPrice(ticket.getPrice());
        ticketEntity.setType(ticket.getType());
        ticketEntity.setComment(ticket.getComment());
        ticketEntity.setCoordinates(mapCoordinates(ticket.getCoordinates()));
        ticketEntity.setPerson(mapPerson(ticket.getPerson()));
        return ticketEntity;
    }
    private CoordinatesEntity mapCoordinates(Coordinates coordinates){
        CoordinatesEntity coordinatesEntity = new CoordinatesEntity();
        coordinatesEntity.setX(coordinates.getX());
        coordinatesEntity.setY(coordinates.getY());
        return coordinatesRepo.save(coordinatesEntity);
    }
    private PersonEntity mapPerson(Person person){
        PersonEntity personEntity = new PersonEntity();
        personEntity.setBirthday(person.getBirthday());
        personEntity.setColorE(person.getColorE());
        personEntity.setColorH(person.getColorH());
        return personRepository.save(personEntity);
    }

}

