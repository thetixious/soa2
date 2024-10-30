package org.tix.soa2.service;

import com.example.model.Ticket;
import org.springframework.stereotype.Service;
import org.tix.soa2.mapper.TicketMapper;
import org.tix.soa2.model.TicketEntity;
import org.tix.soa2.repo.TicketRepository;

@Service
public class TicketsService {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public TicketsService(TicketRepository ticketRepository, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    public void createTicket(Ticket ticket) {
        System.out.println(ticket);

        TicketEntity ticketEntity1 = ticketMapper.toEntity(ticket);
        System.out.println(ticketEntity1);
        ticketRepository.save(ticketEntity1);

    }
}
