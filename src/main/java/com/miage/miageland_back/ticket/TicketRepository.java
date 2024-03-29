package com.miage.miageland_back.ticket;

import com.miage.miageland_back.users.visitor.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByVisitor(Visitor visitor);

    Integer countTicketsByVisitDate(LocalDate date);

    Integer countTicketsByVisitDateAndState(LocalDate date,TicketState ticketState);

    List<Ticket> findByVisitDate(LocalDate date);

    @Query(value = "SELECT COALESCE(MAX(nb_visits_per_day),-1)" +
            "FROM " +
            "    (SELECT COUNT(*) as nb_visits_per_day" +
            "     FROM TICKET" +
            "     WHERE TICKET.VISIT_DATE > CURRENT_TIMESTAMP()" +
            "     GROUP BY TICKET.VISIT_DATE)", nativeQuery = true)
    Integer getMaxTicketsInAFutureDay();

    Integer countTicketsByVisitorAndState(Visitor visitor,TicketState ticketState);

    int countTicketsByState(TicketState ticketState);
}