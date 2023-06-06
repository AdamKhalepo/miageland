package com.miage.miageland_back.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Statistic {

    private double dailyRecipe;

    private int ticketsSoldperDay;

    private int ticketsCancelled;

    private int ticketsOnStandBy;

    private LocalDate date;
}
