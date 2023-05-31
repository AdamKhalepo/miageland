package com.miage.miageland_back.dao.repository;

import com.miage.miageland_back.entities.Ticket;
import com.miage.miageland_back.entities.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByVisitor(Visitor visitor);
}
