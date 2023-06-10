package com.miage.miageland_back.park;


import lombok.Data;

import java.time.LocalDate;

@Data
/**
 * This class is used to store the statistics of the park on a daily basis
 */
public class StatisticDaily extends Statistic {

    private LocalDate date;

    private double dailyRecipe;
}