package com.miage.miageland_back.dao.repository;

import com.miage.miageland_back.entities.Ticket;
import com.miage.miageland_back.entities.Visitor;
import com.miage.miageland_back.enums.TicketState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByVisitor(Visitor visitor);

    Integer countTicketsByVisitDate(LocalDate date);

    Integer countTicketsByState(TicketState ticketState);

    Optional<List<Ticket>> findByVisitDate(LocalDate date);

    Integer countTicketsByVisitorAndState(Visitor visitor,TicketState ticketState);



}
