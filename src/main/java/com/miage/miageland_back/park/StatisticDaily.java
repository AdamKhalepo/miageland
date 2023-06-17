package com.miage.miageland_back.park;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StatisticDaily extends Statistic {

    private LocalDate date;

    private double dailyRecipe;
}