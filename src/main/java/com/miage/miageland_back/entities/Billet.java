package com.miage.miageland_back.entities;

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
public class Billet {

    @Id
    private Long id;

    private Date dateVisite;

    @ManyToOne
    @JoinColumn(name = "visiteur_id")
    private Visiteur visiteur;

    private double prix;

}
