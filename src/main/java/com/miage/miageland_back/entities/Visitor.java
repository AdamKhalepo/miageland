package com.miage.miageland_back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Visitor {
    @Id
    private Long id;

    private String name;

    private String firstName;

    private String email;
}
