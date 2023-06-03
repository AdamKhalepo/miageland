package com.miage.miageland_back.service;

import com.miage.miageland_back.dao.repository.TicketRepository;
import com.miage.miageland_back.entities.Statistic;
import com.miage.miageland_back.entities.Ticket;
import com.miage.miageland_back.enums.TicketState;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ParkService {

    private final TicketRepository ticketRepository;

    public Statistic getStatisticsOfaDate(LocalDate date) {
        Statistic statistic = new Statistic();
        double dailyRecipe = 0;
        for (Ticket ticket : ticketRepository.findByVisitDate(date).orElseThrow(() -> new EntityNotFoundException("Ticket with date : " + date + "  exists"))) {
            if (ticket.getState().equals(TicketState.VALID) || (ticket.getState().equals(TicketState.USED)))
                dailyRecipe = dailyRecipe + ticket.getPrice();
        }
        statistic.setDate(date);
        statistic.setDailyRecipe(dailyRecipe);
        statistic.setTicketsSoldperDay(ticketRepository.countTicketsByVisitDate(date));
        statistic.setTicketsCancelled(ticketRepository.countTicketsByState(TicketState.CANCELLED));
        statistic.setTicketsOnStandBy(ticketRepository.countTicketsByState(TicketState.PENDING_PAYMENT));
        return statistic;
    }
}
