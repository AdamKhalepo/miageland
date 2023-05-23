package com.miage.miageland_back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Employe {

    @Id
    private Long id;

    private String nom;

    private String prenom;

    private String email;
}
