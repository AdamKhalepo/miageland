package com.miage.miageland_back.entities;

import com.miage.miageland_back.TicketState;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    @Id
    private Long id;

    private Date visiteDate;

    @ManyToOne
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;

    private double price;

    private TicketState state;

}
