package org.tix.soa2.controller;

import com.example.api.TicketApi;
import com.example.model.TicketForResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;
import org.tix.soa2.service.TicketService;

import java.util.List;
import java.util.Optional;

@Controller
public class TicketApiController implements TicketApi {
    private final TicketService ticketService;

    public TicketApiController(TicketService ticketService) {
        this.ticketService = ticketService;
    }


    @Override
    public ResponseEntity<List<TicketForResponse>> ticketStartCommentGet(String commentStartsWith) {
        ticketService.getTicketWhichStartByComment(commentStartsWith);
        return TicketApi.super.ticketStartCommentGet(commentStartsWith);
    }
}
