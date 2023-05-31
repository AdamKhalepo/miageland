package com.miage.miageland_back.service;

import com.miage.miageland_back.TicketState;
import com.miage.miageland_back.dao.repository.TicketRepository;
import com.miage.miageland_back.entities.Ticket;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public Ticket validateTicket(int ticketId) {
        Ticket ticket = ticketRepository.findById((long) ticketId).orElseThrow(EntityNotFoundException::new);
        //we have to verify that the Visit date is the current date.
        if (ticket.getState() != TicketState.VALID && ! ticket.getVisitDate().equals(Calendar.getInstance().getTime()))
            throw new IllegalArgumentException("Ticket is not valid");

        //since the ticket is valid, we can update its state to USED
        ticket.setState(TicketState.USED);
        //this line must be deleted
        ticket.setVisitDate(new Date());
        ticketRepository.save(ticket);

        return ticket;
    }
}
