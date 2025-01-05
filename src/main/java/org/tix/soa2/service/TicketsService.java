package org.tix.soa2.service;

import com.example.model.Ticket;
import com.example.model.TicketForComplexResponse;
import com.example.model.TicketForResponse;
import com.example.model.TicketForUserDTO;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tix.soa2.exception.customAdvice.TicketNotFoundException;
import org.tix.soa2.mapper.TicketForComplexResponseMapper;
import org.tix.soa2.mapper.TicketForResponseMapper;
import org.tix.soa2.mapper.TicketForUserDTOMapper;
import org.tix.soa2.mapper.TicketMapper;
import org.tix.soa2.model.PersonEntity;
import org.tix.soa2.model.TicketEntity;
import org.tix.soa2.repo.PersonRepository;
import org.tix.soa2.repo.TicketRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketsService {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final TicketForResponseMapper ticketForResponseMapper;
    private final TicketForComplexResponseMapper ticketForComplexResponseMapper;
    private final PersonRepository personRepository;
    private final TicketForUserDTOMapper ticketForUserDTOMapper;

    public TicketsService(TicketRepository ticketRepository, TicketMapper ticketMapper, TicketForResponseMapper ticketForResponseMapper, TicketForComplexResponseMapper ticketForComplexResponseMapper,
                          PersonRepository personRepository, TicketForUserDTOMapper ticketForUserDTOMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.ticketForResponseMapper = ticketForResponseMapper;
        this.ticketForComplexResponseMapper = ticketForComplexResponseMapper;
        this.personRepository = personRepository;
        this.ticketForUserDTOMapper = ticketForUserDTOMapper;
    }

    public void createTicket(Ticket ticket) {
        TicketEntity ticketEntity = ticketMapper.toEntity(ticket);
        if (ticketEntity.getCreationDate() == null) {
            ticketEntity.setCreationDate(ZonedDateTime.now());
        }
        System.out.println(ticketEntity.getCreationDate());
        ticketRepository.save(ticketEntity);

    }

    @Transactional
    public void deleteTicketById(Long id) {

        ticketRepository.deleteByTicketId(ticketRepository.findById(id).orElseThrow(TicketNotFoundException::new).getId());


    }

    public TicketForResponse getTicketById(Long id) {
        return ticketForResponseMapper.toDTO(ticketRepository.findById(id).orElseThrow(TicketNotFoundException::new));
    }

    @Transactional
    public TicketForResponse deleteTicketByPrice(Float price) {
        TicketEntity ticketEntity = ticketRepository.findFirstByPrice(price).orElseThrow(TicketNotFoundException::new);
        ticketRepository.deleteById(ticketEntity.getId());
        return ticketForResponseMapper.toDTO(ticketEntity);
    }


    public Integer getCountOfTicketWithPrice(Float price) {
        return ticketRepository.countAllByPrice(price).orElseThrow(TicketNotFoundException::new);

    }

    @Transactional
    public void updateTicketsById(Long id, Ticket ticket) {
        TicketEntity oldTicketEntity = ticketRepository.findById(id).orElseThrow(TicketNotFoundException::new);
        TicketEntity newTicketEntity = ticketMapper.toEntity(ticket);
        newTicketEntity.setCreationDate(oldTicketEntity.getCreationDate());
        ticketRepository.updateTicketEntity(id, newTicketEntity);

    }

    public List<TicketForResponse> getTicketThanStartWithComment(String commentStartsWith) {
        return ticketRepository.findAll().stream().filter(ticketEntity -> ticketEntity.getComment().startsWith(commentStartsWith)).map(ticketForResponseMapper::toDTO).collect(Collectors.toList());
    }

    public List<TicketForComplexResponse> getFilteredAndSortedTickets(List<String> sortParams, List<String> filterParams, Long page) {

        Sort sort = parseSortParameters(sortParams);

        Specification<TicketEntity> filterSpecification = parseFilterParameters(filterParams);

        Pageable pageable = PageRequest.of(page.intValue(), 10, sort);

        Page<TicketEntity> ticketsPage = ticketRepository.findAll(filterSpecification, pageable);

        return ticketsPage.stream()
                .map(ticket -> {
                    TicketForComplexResponse response = ticketForComplexResponseMapper.toDTO(ticket);
                    response.setPageNumber((long) ticketsPage.getNumber()); // Устанавливаем номер страницы
                    return response;
                })
                .collect(Collectors.toList());
    }

    private Sort parseSortParameters(List<String> sortParams) {
        if (sortParams == null || sortParams.isEmpty()) {
            return Sort.unsorted();
        }

        List<Sort.Order> orders = sortParams.stream().map(param -> {
            boolean isAsc = param.endsWith("_asc");
            String field = param.replace("_asc", "").replace("_des", "");
            return new Sort.Order(isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, field);
        }).collect(Collectors.toList());

        return Sort.by(orders);
    }

    private Specification<TicketEntity> parseFilterParameters(List<String> filterParams) {
        if (filterParams == null || filterParams.isEmpty()) {
            return Specification.where(null);
        }

        return filterParams.stream()
                .map(this::createFilterSpecification)
                .reduce(Specification::and)
                .orElse(Specification.where(null));
    }

    private Specification<TicketEntity> createFilterSpecification(String filter) {


        int lastUnderscoreBeforeEquals = filter.substring(0, filter.indexOf('=')).lastIndexOf('_');

        String field = filter.substring(0, lastUnderscoreBeforeEquals);           // "person.Color_E"
        String operation = filter.substring(lastUnderscoreBeforeEquals + 1, filter.indexOf('=')); // "eq"
        String value = filter.substring(filter.indexOf('=') + 1);


        System.out.println(field);
        System.out.println(operation);
        System.out.println(value);
        return (root, query, criteriaBuilder) -> {
            Path<Object> path = getPath(root, field);
            return switch (operation) {
                case "eq" -> criteriaBuilder.equal(path, value);
                case "ne" -> criteriaBuilder.notEqual(path, value);
                case "gt" -> criteriaBuilder.greaterThan(path.as(String.class), value);
                case "lt" -> criteriaBuilder.lessThan(path.as(String.class), value);
                case "gte" -> criteriaBuilder.greaterThanOrEqualTo(path.as(String.class), value);
                case "lte" -> criteriaBuilder.lessThanOrEqualTo(path.as(String.class), value);
                default -> throw new IllegalArgumentException("Unsupported operation: " + operation);
            };
        };
    }

    private Path<Object> getPath(Root<TicketEntity> root, String field) {
        if (field.contains(".")) {
            String[] parts = field.split("\\.");
            Path<Object> path = root.get(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }
            return path;
        }
        return root.get(field);
    }



    public void createTicketForPerson(TicketForUserDTO ticket) {
        System.out.println(ticket.getPersonId());
        PersonEntity person = personRepository.findById(ticket.getPersonId()).orElseThrow(TicketNotFoundException::new);
        TicketEntity ticketEntity = ticketForUserDTOMapper.toEntity(ticket, person);
        if (ticketEntity.getCreationDate() == null) {
            ticketEntity.setCreationDate(ZonedDateTime.now());
        }
        System.out.println(ticketEntity);
        ticketRepository.save(ticketEntity);

    }

    @Transactional
    public void deleteTicketByPersonId(Long id) {
        List<TicketEntity> list;
        list = ticketRepository.findAllByPersonId(id);
        System.out.println(list);
        if (list.isEmpty())
            throw new TicketNotFoundException();
        ticketRepository.deleteAll(list);

    }
}
