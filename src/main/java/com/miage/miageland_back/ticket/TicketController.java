package com.miage.miageland_back.ticket;

import com.miage.miageland_back.users.employee.EmployeeService;
import com.miage.miageland_back.users.visitor.VisitorService;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("miageland")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final EmployeeService employeeService;
    private final VisitorService visitorService;

    /**
     * Allows the employee to validate the ticket of a visitor
     * @param userEmail the email of the manager (set in user cookie)
     * @param ticketId the id of the ticket to validate
     */
    @PatchMapping( "employee/tickets/{ticketId}")
    public void patchTicketValidation(@PathVariable Long ticketId,
                                      @CookieValue(value = "user") String userEmail) {
        try {
            if (!this.employeeService.isEmployee(userEmail))
                throw new IllegalAccessException("You must be an employee to call this endpoint.");

            this.ticketService.validateTicket(ticketId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Allows a visitor to create a ticket
     * @param ticket the ticket to create
     * @param visitorId the id of the visitor (set in path)
     * @param userEmail the email of the visitor (set in user cookie)
     * @return the {@link TicketDTO} created
     */
    @PostMapping("visitors/{visitorId}/ticket")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketDTO postTicket(@RequestBody Ticket ticket,
                             @PathVariable Long visitorId,
                             @CookieValue(value = "user") String userEmail) {
        try {
            if (!this.visitorService.isSameVisitor(userEmail, visitorId))
                throw new IllegalAccessException("User email in cookie and user id path must be the same user.");
            if (!this.visitorService.isVisitor(userEmail))
                throw new IllegalAccessException("You must be a visitor to call this endpoint.");

            return this.ticketService.createTicket(ticket, this.visitorService.getVisitorByEmail(userEmail));
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Allows a visitor to pay for a ticket
     * @param ticketId the id of the ticket to pay
     * @param visitorId the id of the visitor (set in path)
     * @param userEmail the email of the visitor (set in user cookie)
     */
    @PatchMapping("visitors/{visitorId}/tickets/{ticketId}/payment")
    public void patchTicketPayment(@PathVariable Long ticketId,
                                   @PathVariable Long visitorId,
                                   @CookieValue(value = "user") String userEmail) {
        try {
            if (!this.visitorService.isSameVisitor(userEmail, visitorId))
                throw new IllegalAccessException("User email in cookie and user id path must be the same user.");
            if (!this.visitorService.isVisitor(userEmail))
                throw new IllegalAccessException("You must be a visitor to call this endpoint.");

            this.ticketService.buyTicket(ticketId, this.visitorService.getVisitorByEmail(userEmail));
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Allows an employee or a visitor to get all his tickets
     * @param visitorId the id of the visitor (set in path)
     * @param userEmail the email of the visitor (set in user cookie)
     * @return a list of {@link TicketDTO}
     */
    @GetMapping("/visitors/{visitorId}/tickets")
    public List<TicketDTO> getUserTickets(@PathVariable Long visitorId,
                                          @CookieValue(value = "user") String userEmail) {
        try {
            //If the user is an employee OR if the user is a visitor and the visitorId is the same as the user's id
            if (!employeeService.isEmployee(userEmail) &&
                    (!this.visitorService.isVisitor(userEmail) || !this.visitorService.isSameVisitor(userEmail, visitorId))) {
                throw new IllegalAccessException("An error occured, try again later.");
            }

            return this.ticketService.getUserTickets(this.visitorService.getVisitorById(visitorId));
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    /**
     * Allows a visitor to cancel a ticket
     * @param visitorId the id of the visitor (set in path)
     * @param ticketId the id of the ticket to cancel
     * @param visitorEmail the email of the visitor (set in user cookie)
     * @return the {@link TicketDTO} canceled
     */
    @PatchMapping("/visitors/{visitorId}/tickets/{ticketId}/cancel")
    public TicketDTO patchTicketCancelation(@PathVariable Long visitorId,
                             @PathVariable Long ticketId,
                             @CookieValue(value = "user") String visitorEmail) {
        try {
            if (!this.visitorService.isVisitor(visitorEmail))
                throw new IllegalAccessException("Log in as a visitor please.");

            if (!this.visitorService.isSameVisitor(visitorEmail, visitorId))
                throw new IllegalAccessException("The visitor email and the visitor id must be from the same visitor");

            return this.ticketService.cancelTicket(visitorId, ticketId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
