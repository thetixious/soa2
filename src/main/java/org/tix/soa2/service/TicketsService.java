package org.tix.soa2.service;

import com.example.model.Ticket;
import com.example.model.TicketForComplexResponse;
import com.example.model.TicketForResponse;
import com.example.model.TicketForUserDTO;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
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
import java.util.ArrayList;
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
        ticketRepository.save(ticketEntity);

    }
    @Transactional
    public void deleteTicketById(Long id) {
        if (ticketRepository.findById(id).isPresent()) {
            ticketRepository.deleteByTicketId(id);
        }

    }

    public TicketForResponse getTicketById(Long id) {
        return ticketForResponseMapper.toDTO(ticketRepository.findById(id).orElseThrow(TicketNotFoundException::new));
    }

    @Transactional
    public TicketForResponse deleteTicketByPrice(Double price) {
        TicketEntity ticketEntity = ticketRepository.findByPrice(price).orElseThrow(TicketNotFoundException::new);
        ticketRepository.deleteById(ticketEntity.getId());
        return ticketForResponseMapper.toDTO(ticketEntity);
    }


    public Integer getCountOfTicketWithPrice(Double price) {
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

        Pageable pageable = PageRequest.of(page.intValue(), 10, getSort(sortParams));
        System.out.println(pageable);
        Specification<TicketEntity> specification = getSpecification(filterParams);

        Page<TicketEntity> ticketPage = ticketRepository.findAll(specification, pageable);
        return ticketPage.stream()
                .map(ticket -> {
                    TicketForComplexResponse response = ticketForComplexResponseMapper.toDTO(ticket);
                    response.setPageNumber((long) ticketPage.getNumber()); // Устанавливаем номер страницы
                    return response;
                })
                .collect(Collectors.toList());
    }

    private Sort getSort(List<String> sortParams) {
        List<Sort.Order> orders = new ArrayList<>();
        for (String param : sortParams) {
            String[] parts = param.split("_");
            if (parts.length == 2) {
                Sort.Direction direction = "asc".equalsIgnoreCase(parts[1]) ? Sort.Direction.ASC : Sort.Direction.DESC;
                orders.add(new Sort.Order(direction, parts[0]));
            }
        }
        return Sort.by(orders);
    }

    private Specification<TicketEntity> getSpecification(List<String> filterParams) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (String filter : filterParams) {
                String[] parts = filter.split("_");
                String field = parts[0];
                String[] parts2 = parts[1].split("=");
                String operator = parts2[0];
                String value = parts2[1];
                Path<?> path;
                if (field.contains(".")) {

                    String[] fieldParts = field.split("\\.");
                    Join<Object, Object> join = root.join(fieldParts[0], JoinType.LEFT); // создаем join для вложенного объекта
                    path = join.get(fieldParts[1]);
                } else {
                    path = root.get(field);
                }

                switch (operator) {
                    case "gt":
                        predicates.add(cb.greaterThan(path.as(Long.class), Long.parseLong(value)));
                        break;
                    case "lt":
                        predicates.add(cb.lessThan(path.as(Long.class), Long.parseLong(value)));
                        break;
                    case "eq":
                        predicates.add(cb.equal(path, Long.parseLong(value)));
                        break;
                    case "ne":
                        predicates.add(cb.notEqual(path, Long.parseLong(value)));
                        break;
                    case "gte":
                        predicates.add(cb.greaterThanOrEqualTo(path.as(Long.class), Long.parseLong(value)));
                        break;
                    case "lte":
                        predicates.add(cb.lessThanOrEqualTo(path.as(Long.class), Long.parseLong(value)));
                        break;
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    public void createTicketForPerson(TicketForUserDTO ticket) {
        System.out.println(ticket.getPersonId());
        PersonEntity person = personRepository.findById(ticket.getPersonId()).orElseThrow(TicketNotFoundException::new);
        TicketEntity ticketEntity = ticketForUserDTOMapper.toEntity(ticket,person);
        if (ticketEntity.getCreationDate() == null) {
            ticketEntity.setCreationDate(ZonedDateTime.now());
        }
        System.out.println(ticketEntity);
        ticketRepository.save(ticketEntity);

    }
    @Transactional
    public void deleteTicketByPersonId(Long id) {
        ticketRepository.deleteByPersonId(id);
    }
}
