package com.miage.miageland_back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VisitorDTO {

    private Long id;

    private String name;

    private String lastName;

    private int nbVisite;
}
