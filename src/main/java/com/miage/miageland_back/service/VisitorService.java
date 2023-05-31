package com.miage.miageland_back.service;

import com.miage.miageland_back.TicketState;
import com.miage.miageland_back.dao.repository.TicketRepository;
import com.miage.miageland_back.dao.repository.VisitorRepository;
import com.miage.miageland_back.entities.Ticket;
import com.miage.miageland_back.entities.Visitor;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitorService {
    private final VisitorRepository visitorRepository;
    private final CookieService cookieService;
    private final TicketRepository ticketRepository;

    public void loginVisitor(String visitorEmail, HttpServletResponse response) {
        if (!visitorRepository.existsByEmail(visitorEmail))
            throw new EntityNotFoundException("Employee does not exist");

        Visitor loggedVisitor = visitorRepository.findByEmail(visitorEmail);

        cookieService.deleteCookies(response);
        //Adding cookie to response to keep track of the employee
        //This cookie needs to be sent back to the server to identify the employee
        cookieService.addVisitorCookie(loggedVisitor, response);
    }

    //TO REFINE
    public void addTicket(Long id, Ticket ticket) {

        Visitor visitor = visitorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee with id=" + id + " does not exist"));
        ticket.setVisitor(visitor);
        ticket.setState(TicketState.PENDING_PAYMENT);
        ticketRepository.save(ticket);
    }

    //TO REFINE
    public void buyTicket(Long id) {

       Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ticket with id=" + id + " does not exist"));
        if(!ticket.getState().equals(TicketState.PENDING_PAYMENT))
            throw new EntityNotFoundException("The ticket must be reserved");
        ticket.setState(TicketState.VALID);
        ticketRepository.save(ticket);
    }

    //TO REFINE
    public void deleteTicket(Long id) {
         ticketRepository.deleteById(id);
        if(ticketRepository.findById(id).isPresent())
            throw new EntityNotFoundException("The ticket is not canceled");

    }





}
