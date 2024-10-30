package org.tix.soa2.controller;

import com.example.api.TicketsApi;
import com.example.model.Ticket;
import com.example.model.TicketForComplexResponse;
import com.example.model.TicketForResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TicketsApiController implements TicketsApi {



    @Override
    public ResponseEntity<List<TicketForComplexResponse>> ticketsGet(List<String> sort, List<String> filter, Integer page) {
        return TicketsApi.super.ticketsGet(sort, filter, page);
    }

    @Override
    public ResponseEntity<Void> ticketsIdDelete(Integer id) {
        return TicketsApi.super.ticketsIdDelete(id);
    }

    @Override
    public ResponseEntity<Void> ticketsIdGet(Integer id) {
        return TicketsApi.super.ticketsIdGet(id);
    }

    @Override
    public ResponseEntity<Void> ticketsIdPut(Integer id, Ticket ticket) {
        return TicketsApi.super.ticketsIdPut(id, ticket);
    }

    @Override
    public ResponseEntity<Void> ticketsPost(Ticket ticket) {

        return TicketsApi.super.ticketsPost(ticket);
    }

    @Override
    public ResponseEntity<TicketForResponse> ticketsPriceDelete(Integer price) {
        return TicketsApi.super.ticketsPriceDelete(price);
    }

    @Override
    public ResponseEntity<Integer> ticketsPriceGet(Integer price) {
        return TicketsApi.super.ticketsPriceGet(price);
    }
}
