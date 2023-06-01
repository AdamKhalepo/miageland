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
@RequestMapping("miageland")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final EmployeeService employeeService;
    private final VisitorService visitorService;

    //TODO : FIND a better endpoint name
    @PatchMapping( "/tickets/employee/{ticketId}")
    public void patchTicketValidation(@PathVariable Long ticketId,
                            @CookieValue(value = "user") String userEmail) throws IllegalAccessException {
        if (!this.employeeService.isEmployee(userEmail))
            throw new IllegalAccessException("You must be an employee to call this endpoint.");
        this.ticketService.validateTicket(ticketId);
    }

    @PostMapping("/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket postTicket(@RequestBody Ticket ticket,
                           @CookieValue(value = "user") String userEmail) throws IllegalAccessException {
        if (!this.visitorService.isVisitor(userEmail))
            throw new IllegalAccessException("You must be a visitor to call this endpoint.");
        return this.ticketService.createTicket(ticket, this.visitorService.getVisitorByEmail(userEmail));
    }

    @PatchMapping("/tickets/{ticketId}")
    public void patchTicketPayment(@PathVariable Long ticketId,
                            @CookieValue(value = "user") String userEmail) throws IllegalAccessException {
        if (!this.visitorService.isVisitor(userEmail))
            throw new IllegalAccessException("You must be a visitor to call this endpoint.");

        this.ticketService.buyTicket(ticketId, this.visitorService.getVisitorByEmail(userEmail));
    }

    @GetMapping("/visitors/{visitorId}/tickets")
    public List<Ticket> getUserTickets(@PathVariable Long visitorId) {
        return this.ticketService.getUserTickets(this.visitorService.getVisitorById(visitorId));
    }

    //to refine
    @PatchMapping("/visitors/{visitorId}/tickets/{ticketId}")
    public void deleteTicket(@PathVariable Long visitorId,
                             @PathVariable Long ticketId) {
        this.ticketService.cancelTicket(visitorId,ticketId);
    }
}
