package com.miage.miageland_back.service;

import com.miage.miageland_back.enums.TicketState;
import com.miage.miageland_back.dao.repository.TicketRepository;
import com.miage.miageland_back.dao.repository.VisitorRepository;
import com.miage.miageland_back.entities.Ticket;
import com.miage.miageland_back.entities.Visitor;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final VisitorRepository visitorRepository;

    public void validateTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).
                orElseThrow(() -> new EntityNotFoundException("Ticket with id : " + ticketId + " does not exist"));
        //we have to verify that the Visit date is the current date.
        if (ticket.getState() != TicketState.VALID && ! ticket.getVisitDate().equals(Calendar.getInstance().getTime()))
            throw new IllegalArgumentException("Ticket is not valid");

        //since the ticket is valid, we can update its state to USED
        ticket.setState(TicketState.USED);
        //this line must be deleted
        ticket.setVisitDate(new Date());
        ticketRepository.save(ticket);
    }

    //TO REFINE
    public Ticket createTicket(Ticket ticket, Visitor visitor) {
        //TODO : add verification with the limit (max ticket per day)
        if (ticket.getVisitDate().before(Calendar.getInstance().getTime()))
            throw new IllegalArgumentException("The visit date must be in the future");
        if (ticket.getPrice() < 0)
            throw new IllegalArgumentException("The price must be positive");
        if (!visitorRepository.existsById(visitor.getId())) {
            throw new EntityNotFoundException("Visitor with id : " + visitor.getId() + " does not exist");
        }

        ticket.setVisitor(visitor);
        ticket.setState(TicketState.PENDING_PAYMENT);
        ticketRepository.save(ticket);
        return ticket;
    }

    //TO REFINE
    public void buyTicket(Long id, Visitor loggedVisitor) {
        Ticket ticket = ticketRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Ticket with id : " + id + " does not exist"));
        if(!ticket.getVisitor().equals(loggedVisitor))
            throw new IllegalStateException("The ticket does not belong to the visitor");
        if(!ticket.getState().equals(TicketState.PENDING_PAYMENT))
            throw new EntityNotFoundException("The ticket must be reserved");
        ticket.setState(TicketState.VALID);
        ticketRepository.save(ticket);
    }

    //TO REFINE
    public void cancelTicket(Long visitorId, Long ticketId) {
        //todo : handle the date of the refund
        Ticket ticket = ticketRepository.findById(ticketId).
                orElseThrow(() -> new EntityNotFoundException("Ticket with id : " + ticketId + " does not exist"));
        if (!ticket.getVisitor().getId().equals(visitorId))
            throw new IllegalStateException("The ticket does not belong to the visitor");

        System.out.println(!ticket.getState().equals(TicketState.VALID));
        System.out.println(!ticket.getState().equals(TicketState.PENDING_PAYMENT));
        if (!(ticket.getState().equals(TicketState.VALID) || ticket.getState().equals(TicketState.PENDING_PAYMENT)))
            throw new IllegalStateException("The ticket cannot be cancelled");
        ticket.setState(TicketState.CANCELLED);
    }

    public List<Ticket> getUserTickets(Visitor visitor) {
        return this.ticketRepository.findByVisitor(visitor);
    }
}
