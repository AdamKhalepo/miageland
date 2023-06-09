package com.miage.miageland_back.users.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String email;
    private EmployeeRole role;
}
