package org.tix.soa2.controller;

import com.example.api.TicketsApi;
import com.example.model.Ticket;
import com.example.model.TicketForComplexResponse;
import com.example.model.TicketForResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.tix.soa2.service.TicketsService;

import java.util.List;


@Controller
@RequestMapping("/tickets")
public class TicketsApiController implements TicketsApi {

    private final TicketsService ticketsService;

    public TicketsApiController(TicketsService ticketsService) {
        this.ticketsService = ticketsService;
    }


    @Override
    public ResponseEntity<List<TicketForComplexResponse>> ticketsGet(List<String> sort, List<String> filter, Long page) {
        return TicketsApi.super.ticketsGet(sort, filter, page);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<String> ticketsIdDelete(@PathVariable Long id) {
        ticketsService.deleteTicketById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Билет удален успешно");
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<TicketForResponse> ticketsIdGet(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(ticketsService.getTicketById(id));
    }


    @PutMapping("/{id}")
    @Override
    public ResponseEntity<String> ticketsIdPut(@PathVariable Long id, Ticket ticket) {
        ticketsService.updateTicketsById(id,ticket);
        return ResponseEntity.status(HttpStatus.OK).body("Билет обновлен успешно");
    }

    @GetMapping("/start/comment")
    @Override
    public ResponseEntity<List<TicketForResponse>> ticketsStartCommentGet(@RequestParam(name = "commentStartsWith") String commentStartsWith) {
        return ResponseEntity.status(HttpStatus.OK).body(ticketsService.getTicketThanStartWithComment(commentStartsWith));
    }

    @PostMapping
    @Override
    public ResponseEntity<String> ticketsPost(Ticket ticket) {
        ticketsService.createTicket(ticket);
        return ResponseEntity.status(HttpStatus.OK).body("Билет создан успешно");
    }

    @DeleteMapping("/byPrice/{price}")
    @Override
    public ResponseEntity<TicketForResponse> ticketsByPricePriceDelete(@PathVariable Integer price) {

        return ResponseEntity.status(HttpStatus.OK).body(ticketsService.deleteTicketByPrice(price));
    }

    @GetMapping("/byPrice/{price}")
    @Override
    public ResponseEntity<Integer> ticketsByPricePriceGet(@PathVariable Integer price) {

        return ResponseEntity.status(HttpStatus.OK).body(ticketsService.getCountOfTicketWithPrice(price));
    }


}
