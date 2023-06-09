package com.miage.miageland_back.park;


import lombok.Data;

import java.time.LocalDate;

@Data
public class Statistic {
    int ticketsSoldperDay;
    private LocalDate date;
    private double dailyRecipe;
    private int ticketsCancelled;
    private int ticketsOnStandBy;
}


