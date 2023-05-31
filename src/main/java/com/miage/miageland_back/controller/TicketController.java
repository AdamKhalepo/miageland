package com.miage.miageland_back.controller;

import com.miage.miageland_back.entities.Ticket;
import com.miage.miageland_back.service.EmployeeService;
import com.miage.miageland_back.service.TicketService;
import com.miage.miageland_back.service.VisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("miageland/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final EmployeeService employeeService;
    private final VisitorService visitorService;

    //TODO : FIND a better endpoint name
    @PatchMapping( "/employee/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    public void patchTicketValidation(@PathVariable Long ticketId,
                            @CookieValue(value = "user") String userEmail) throws IllegalAccessException {
        if (!this.employeeService.isEmployee(userEmail))
            throw new IllegalAccessException("You must be an employee to call this endpoint.");
        this.ticketService.validateTicket(ticketId);
    }

    @PostMapping
    public Ticket postTicket(@RequestBody Ticket ticket,
                           @CookieValue(value = "user") String userEmail) throws IllegalAccessException {
        if (!this.visitorService.isVisitor(userEmail))
            throw new IllegalAccessException("You must be a visitor to call this endpoint.");
        return this.ticketService.createTicket(ticket, this.visitorService.getVisitor(userEmail));
    }

    @PatchMapping("/{ticketId}")
    public void patchTicketPayment(@PathVariable Long ticketId,
                            @CookieValue(value = "user") String userEmail) throws IllegalAccessException {
        if (!this.visitorService.isVisitor(userEmail))
            throw new IllegalAccessException("You must be a visitor to call this endpoint.");

        this.ticketService.buyTicket(ticketId, this.visitorService.getVisitor(userEmail));
    }

    @GetMapping
    public List<Ticket> getUserTickets(@CookieValue(value = "user") String userEmail) throws IllegalAccessException {
        if (!this.visitorService.isVisitor(userEmail))
            throw new IllegalAccessException("You must be a visitor to call this endpoint.");
        return this.ticketService.getUserTickets(this.visitorService.getVisitor(userEmail));
    }

    //to refine
    @DeleteMapping("/{id}/tickets/{idTicket}")
    public void deleteTicket(@PathVariable Long idTicket){
        this.ticketService.refundTicket(idTicket);
    }
}
