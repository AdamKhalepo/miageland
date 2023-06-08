package com.miage.miageland_back.dto;

import com.miage.miageland_back.enums.TicketState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TicketDTO {
    private Long id;

    private LocalDate visitDate;

    private String visitorEmail;

    private double price;

    private TicketState state;
}
