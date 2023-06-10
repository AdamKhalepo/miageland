package com.miage.miageland_back.park;

import com.miage.miageland_back.ticket.TicketRepository;
import com.miage.miageland_back.ticket.Ticket;
import com.miage.miageland_back.ticket.TicketState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ParkService {

    private final TicketRepository ticketRepository;

    public StatisticDaily getStatisticsOfADate(LocalDate date) {
        StatisticDaily statistic = new StatisticDaily();
        double dailyRecipe = 0;
        for (Ticket ticket : ticketRepository.findByVisitDate(date)) {
            if (ticket.getState().equals(TicketState.VALID) || (ticket.getState().equals(TicketState.USED)))
                dailyRecipe = dailyRecipe + ticket.getPrice();
        }
        statistic.setDate(date);
        statistic.setDailyRecipe(dailyRecipe);
        statistic.setNbTicketsUsed(ticketRepository.countTicketsByVisitDateAndState(date,TicketState.USED));
        statistic.setNbTicketsSold(ticketRepository.countTicketsByVisitDateAndState(date,TicketState.VALID));
        statistic.setNbTicketsCancelled(ticketRepository.countTicketsByVisitDateAndState(date,TicketState.CANCELLED));
        statistic.setNbTicketsOnStandBy(ticketRepository.countTicketsByVisitDateAndState(date,TicketState.PENDING_PAYMENT));
        return statistic;
    }

    public void updateGauge(int gauge) {
        //Get the instance of the park (singleton)
        Park park = Park.getInstance();
        // -1 means that there is no ticket sold yet
        if (this.ticketRepository.getMaxTicketsInAFutureDay() != -1) {
            if (gauge < this.ticketRepository.getMaxTicketsInAFutureDay())
                throw new IllegalArgumentException("The gauge needs to be higher or equal than the max tickets in a day");
        }
        park.setGauge(gauge);
    }

    public Statistic getGlobalStatistics() {
        Statistic statistic = new Statistic();
        statistic.setNbTicketsUsed(ticketRepository.countTicketsByState(TicketState.USED));
        statistic.setNbTicketsSold(ticketRepository.countTicketsByState(TicketState.VALID));
        statistic.setNbTicketsCancelled(ticketRepository.countTicketsByState(TicketState.CANCELLED));
        statistic.setNbTicketsOnStandBy(ticketRepository.countTicketsByState(TicketState.PENDING_PAYMENT));
        return statistic;
    }
}
