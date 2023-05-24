package com.miage.miageland_back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Park {

    @Id
    private int id;

    private int jaugeMax;

    private int nbMaxJournalier;
}
