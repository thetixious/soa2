package org.tix.soa2.mapper;

import com.example.model.Coordinates;
import com.example.model.Person;
import org.springframework.stereotype.Component;
import org.tix.soa2.model.CoordinatesEntity;
import org.tix.soa2.model.PersonEntity;
import org.tix.soa2.repo.CoordinatesRepo;
import org.tix.soa2.repo.PersonRepository;
@Component
public class UtilMapper {
    private final CoordinatesRepo coordinatesRepo;
    private final PersonRepository personRepository;

    public UtilMapper(CoordinatesRepo coordinatesRepo, PersonRepository personRepository) {
        this.coordinatesRepo = coordinatesRepo;
        this.personRepository = personRepository;
    }

    public CoordinatesEntity mapCoordinatesToCoordinatesEntity(Coordinates coordinates){
        CoordinatesEntity coordinatesEntity = new CoordinatesEntity();
        coordinatesEntity.setX(coordinates.getX());
        System.out.println(coordinates.getX());
        coordinatesEntity.setY(coordinates.getY());
        return coordinatesRepo.save(coordinatesEntity);
    }
    public PersonEntity mapPersonToPersonEntity(Person person){
        PersonEntity personEntity = new PersonEntity();
        personEntity.setBirthday(person.getBirthday());
        personEntity.setColorE(person.getColorE());
        personEntity.setColorH(person.getColorH());
        return personRepository.save(personEntity);
    }
    public Coordinates mapCoordinatesEntityToCoordinates(CoordinatesEntity coordinatesEntity){
        Coordinates coordinates = new Coordinates();
        coordinates.setX(coordinatesEntity.getX());
        coordinates.setY(coordinatesEntity.getY());
        return coordinates;

    }
    public Person mapPersonEntityToPerson(PersonEntity personEntity){
        Person person = new Person();
        person.setBirthday(personEntity.getBirthday());
        person.setColorE(personEntity.getColorE());
        person.setColorH(personEntity.getColorH());
        return person;
    }
}
