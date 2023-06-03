package com.miage.miageland_back.entities;

import com.miage.miageland_back.enums.TicketState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate visitDate;

    @ManyToOne
    private Visitor visitor;

    private double price;

    private TicketState state;

}
