package com.miage.miageland_back.park;


import lombok.Data;

@Data
/**
 * This class is used to store the statistics of the park
 */
public class Statistic {

    private int nbTicketsCancelled;

    private int nbTicketsOnStandBy;

    private int nbTicketsSold;

    private int nbTicketsUsed;
}


