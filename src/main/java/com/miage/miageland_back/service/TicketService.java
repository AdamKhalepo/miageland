package com.miage.miageland_back.service;

import com.miage.miageland_back.TicketState;
import com.miage.miageland_back.dao.repository.TicketRepository;
import com.miage.miageland_back.entities.Ticket;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public Ticket validateTicket(int ticketId) {
        Ticket ticket = ticketRepository.findById((long) ticketId).orElseThrow(EntityNotFoundException::new);

        if (ticket.getState() != TicketState.VALID)
            throw new IllegalArgumentException("Ticket is not valid");

        //since the ticket is valid, we can update its state to USED
        ticket.setState(TicketState.USED);
        ticket.setVisitDate(new Date());
        ticketRepository.save(ticket);

        return ticket;
    }
}
