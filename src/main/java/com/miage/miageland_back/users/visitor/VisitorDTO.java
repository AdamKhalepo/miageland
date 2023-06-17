package com.miage.miageland_back.users.visitor;

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

    private int nbVisit;
}
