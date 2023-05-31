package com.miage.miageland_back.dto;

import com.miage.miageland_back.enums.EmployeeRole;
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
