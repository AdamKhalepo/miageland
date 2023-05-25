package com.miage.miageland_back.controller;

import com.miage.miageland_back.TicketState;
import com.miage.miageland_back.entities.Ticket;
import com.miage.miageland_back.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("miageland/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    //todo: add annotation to check the user is an employee
    //TODO : return in another way ? full ticket is not needed
    @PatchMapping( "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Ticket patchTicket(@PathVariable int id) {
        return ticketService.validateTicket(id);
    }
}
