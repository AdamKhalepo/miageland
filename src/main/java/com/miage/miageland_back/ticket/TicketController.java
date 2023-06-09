package com.miage.miageland_back.ticket;

import com.miage.miageland_back.users.employee.EmployeeService;
import com.miage.miageland_back.users.visitor.VisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("miageland")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final EmployeeService employeeService;
    private final VisitorService visitorService;

    @PatchMapping( "employee/tickets/{ticketId}")
    public void patchTicketValidation(@PathVariable Long ticketId,
                                      @CookieValue(value = "user") String userEmail) throws IllegalAccessException {
        if (!this.employeeService.isEmployee(userEmail))
            throw new IllegalAccessException("You must be an employee to call this endpoint.");

        this.ticketService.validateTicket(ticketId);
    }

    @PostMapping("visitors/{visitorId}/ticket")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketDTO postTicket(@RequestBody Ticket ticket,
                             @PathVariable Long visitorId,
                             @CookieValue(value = "user") String userEmail) throws IllegalAccessException {
        if (!this.visitorService.isSameVisitor(userEmail,visitorId))
            throw new IllegalAccessException("User email in cookie and user id path must be the same user.");
        if (!this.visitorService.isVisitor(userEmail))
            throw new IllegalAccessException("You must be a visitor to call this endpoint.");

        return this.ticketService.createTicket(ticket, this.visitorService.getVisitorByEmail(userEmail));
    }

    @PatchMapping("visitors/{visitorId}/tickets/{ticketId}/payment")
    public void patchTicketPayment(@PathVariable Long ticketId,
                                   @PathVariable Long visitorId,
                                   @CookieValue(value = "user") String userEmail) throws IllegalAccessException {
        if (!this.visitorService.isSameVisitor(userEmail,visitorId))
            throw new IllegalAccessException("User email in cookie and user id path must be the same user.");
        if (!this.visitorService.isVisitor(userEmail))
            throw new IllegalAccessException("You must be a visitor to call this endpoint.");

        this.ticketService.buyTicket(ticketId, this.visitorService.getVisitorByEmail(userEmail));
    }

    @GetMapping("/visitors/{visitorId}/tickets")
    public List<TicketDTO> getUserTickets(@PathVariable Long visitorId,
                                          @CookieValue(value = "user") String userEmail) throws IllegalAccessException {
        //If the user is an employee OR if the user is a visitor and the visitorId is the same as the user's id
        if (!employeeService.isEmployee(userEmail) &&
                (!this.visitorService.isVisitor(userEmail) || !this.visitorService.isSameVisitor(userEmail, visitorId))) {
            throw new IllegalAccessException("An error occured, try again later.");
        }

        return this.ticketService.getUserTickets(this.visitorService.getVisitorById(visitorId));
    }

    @PatchMapping("/visitors/{visitorId}/tickets/{ticketId}/cancel")
    public TicketDTO patchTicketCancelation(@PathVariable Long visitorId,
                             @PathVariable Long ticketId,
                             @CookieValue(value = "user") String userEmail) throws IllegalAccessException {
        //If the user is an employee OR if the user is a visitor and the visitorId is the same as the user's id
        if (!employeeService.isEmployee(userEmail) &&
                (!this.visitorService.isVisitor(userEmail) || !this.visitorService.isSameVisitor(userEmail, visitorId))) {
            throw new IllegalAccessException("An error occured, try again later.");
        }

        return this.ticketService.cancelTicket(visitorId,ticketId);
    }
}
