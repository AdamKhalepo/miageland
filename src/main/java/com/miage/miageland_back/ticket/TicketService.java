package com.miage.miageland_back.ticket;

import com.miage.miageland_back.users.visitor.VisitorRepository;
import com.miage.miageland_back.park.Park;
import com.miage.miageland_back.users.visitor.Visitor;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final VisitorRepository visitorRepository;
    private final Park park;

    /**
     * Validate a ticket
     * @param ticketId the id of the ticket to validate
     */
    public void validateTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).
                orElseThrow(() -> new EntityNotFoundException("Ticket with id : " + ticketId + " does not exist"));
        //we have to verify that the Visit date is the current date.
        if (ticket.getState() != TicketState.VALID && !ticket.getVisitDate().equals(LocalDate.now()))
            throw new IllegalArgumentException("Ticket is not valid");

        //since the ticket is valid, we can update its state to USED
        ticket.setState(TicketState.USED);
        ticketRepository.save(ticket);
    }

    /**
     * Create a ticket
     * @param ticket the ticket to create
     * @param visitor the visitor who wants to create the ticket
     * @return the created ticket
     */
    public TicketDTO createTicket(Ticket ticket, Visitor visitor) {
        //we have to verify that the Visit date is before the current date.
        if (ticket.getVisitDate().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("The visit date must be in the future");

        //we have to verify that the gauge of the park is not exceeded
        if (park.getGauge() != null) {
            if (ticketRepository.countTicketsByVisitDate(ticket.getVisitDate()) >= park.getGauge())
                throw new IllegalArgumentException("The gauge of the park is exceeded");
        }

        //we have to verify that the price is positive
        if (ticket.getPrice() < 0)
            throw new IllegalArgumentException("The price must be positive");

        //we have to verify that the visitor Id exists
        if (!visitorRepository.existsById(visitor.getId())) {
            throw new EntityNotFoundException("Visitor with id : " + visitor.getId() + " does not exist");
        }

        ticket.setVisitor(visitor);
        ticket.setState(TicketState.PENDING_PAYMENT);
        ticketRepository.save(ticket);

        return new TicketDTO(ticket.getId(),
                ticket.getVisitDate(),
                ticket.getVisitor().getEmail(),
                ticket.getPrice(),
                ticket.getState());
    }

    /**
     * Allow a visitor to buy a ticket
     * @param id the id of the ticket to buy
     * @param loggedVisitor the visitor who wants to buy the ticket
     */
    public void buyTicket(Long id, Visitor loggedVisitor) {
        Ticket ticket = ticketRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Ticket with id : " + id + " does not exist"));
        if (!ticket.getVisitor().equals(loggedVisitor))
            throw new IllegalStateException("The ticket does not belong to the visitor");
        if (!ticket.getState().equals(TicketState.PENDING_PAYMENT))
            throw new EntityNotFoundException("The ticket must be reserved");
        ticket.setState(TicketState.VALID);
        ticketRepository.save(ticket);
    }

    /**
     * Cancel a ticket
     * @param visitorId the id of the visitor
     * @param ticketId the id of the ticket to cancel
     * @return the cancelled ticket
     */
    public TicketDTO cancelTicket(Long visitorId, Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).
                orElseThrow(() -> new EntityNotFoundException("Ticket with id : " + ticketId + " does not exist"));
        if (!ticket.getVisitor().getId().equals(visitorId))
            throw new IllegalStateException("The ticket does not belong to the visitor");

        //if the ticket is less than 7 days before the visit date, it cannot be cancelled
        if (LocalDate.now().plusDays(7).isAfter(ticket.getVisitDate()))
            throw new IllegalStateException("The ticket cannot be cancelled less than 7 days before the visit date");

        if (!(ticket.getState().equals(TicketState.VALID) || ticket.getState().equals(TicketState.PENDING_PAYMENT)))
            throw new IllegalStateException("The ticket cannot be cancelled");
        ticket.setState(TicketState.CANCELLED);
        ticketRepository.save(ticket);

        return new TicketDTO(ticket.getId(),
                ticket.getVisitDate(),
                ticket.getVisitor().getEmail(),
                ticket.getPrice(),
                ticket.getState());
    }

    /**
     * Get all the tickets of a visitor
     * @param visitor the visitor
     * @return the list of tickets
     */
    public List<TicketDTO> getUserTickets(Visitor visitor) {
        return this.ticketRepository.findByVisitor(visitor).stream().map(ticket ->
                new TicketDTO(ticket.getId(),
                        ticket.getVisitDate(),
                        ticket.getVisitor().getEmail(),
                        ticket.getPrice(),
                        ticket.getState()))
                .toList();
    }
}
