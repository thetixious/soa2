package org.tix.soa2.service;

import com.example.model.Ticket;
import com.example.model.TicketForResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tix.soa2.mapper.TicketForResponseMapper;
import org.tix.soa2.mapper.TicketMapper;
import org.tix.soa2.model.TicketEntity;
import org.tix.soa2.repo.TicketRepository;
import java.time.ZonedDateTime;

@Service
public class TicketsService {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final TicketForResponseMapper ticketForResponseMapper;
    public TicketsService(TicketRepository ticketRepository, TicketMapper ticketMapper, TicketForResponseMapper ticketForResponseMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.ticketForResponseMapper = ticketForResponseMapper;
    }

    public void createTicket(Ticket ticket) {
        TicketEntity ticketEntity = ticketMapper.toEntity(ticket);
        if (ticketEntity.getCreationDate() == null){
            ticketEntity.setCreationDate(ZonedDateTime.now());
        }
        ticketRepository.save(ticketEntity);

    }
    public void deleteTicketById(Long id){
        if (ticketRepository.findById(id).isPresent()){
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

    public void updateTicketsById(Long id, Ticket ticket) {
    }
}
