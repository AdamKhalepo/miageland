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

    public TicketDTO createTicket(Ticket ticket, Visitor visitor) {
        //we have to verify that the Visit date is before the current date.
        if (ticket.getVisitDate().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("The visit date must be in the future");

        //we have to verify that the gauge of the park is not exceeded
        if (Park.getInstance().getGauge() != null) {
            if (ticketRepository.countTicketsByVisitDate(ticket.getVisitDate()) >= Park.getInstance().getGauge())
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
