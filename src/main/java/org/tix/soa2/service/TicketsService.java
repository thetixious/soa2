package org.tix.soa2.service;

import com.example.model.Ticket;
import com.example.model.TicketForComplexResponse;
import com.example.model.TicketForResponse;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tix.soa2.mapper.TicketForComplexResponseMapper;
import org.tix.soa2.mapper.TicketForResponseMapper;
import org.tix.soa2.mapper.TicketMapper;
import org.tix.soa2.model.TicketEntity;
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

    public TicketsService(TicketRepository ticketRepository, TicketMapper ticketMapper, TicketForResponseMapper ticketForResponseMapper, TicketForComplexResponseMapper ticketForComplexResponseMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.ticketForResponseMapper = ticketForResponseMapper;
        this.ticketForComplexResponseMapper = ticketForComplexResponseMapper;
    }

    public void createTicket(Ticket ticket) {
        TicketEntity ticketEntity = ticketMapper.toEntity(ticket);
        if (ticketEntity.getCreationDate() == null) {
            ticketEntity.setCreationDate(ZonedDateTime.now());
        }
        ticketRepository.save(ticketEntity);

    }

    public void deleteTicketById(Long id) {
        if (ticketRepository.findById(id).isPresent()) {
            ticketRepository.deleteById(id);
        }
        System.out.println("Ашибка");

    }

    public TicketForResponse getTicketById(Long id) {
        return ticketForResponseMapper.toDTO(ticketRepository.findById(id).orElseThrow());
    }

    @Transactional
    public TicketForResponse deleteTicketByPrice(Integer price) {
        TicketEntity ticketEntity = ticketRepository.findByPrice(price).orElseThrow();
        ticketRepository.deleteById(ticketEntity.getId());
        return ticketForResponseMapper.toDTO(ticketEntity);
    }


    public Integer getCountOfTicketWithPrice(Integer price) {
        return ticketRepository.countAllByPrice(price).orElseThrow();

    }

    @Transactional
    public void updateTicketsById(Long id, Ticket ticket) {
        TicketEntity oldTicketEntity = ticketRepository.findById(id).orElseThrow();
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
                .map(ticketForComplexResponseMapper::toDTO)
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


                switch (operator) {
                    case "gt":
                        predicates.add(cb.greaterThan(root.get(field), value));
                        break;
                    case "lt":
                        predicates.add(cb.lessThan(root.get(field), value));
                        break;
                    case "eq":
                        predicates.add(cb.equal(root.get(field), value));
                        break;
                    case "ne":
                        predicates.add(cb.notEqual(root.get(field), value));
                        break;
                    case "gte":
                        predicates.add(cb.greaterThanOrEqualTo(root.get(field),value));
                        break;
                    case "lte":
                        predicates.add(cb.lessThanOrEqualTo(root.get(field),value));

                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}
