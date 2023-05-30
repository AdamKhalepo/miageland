package com.miage.miageland_back.controller;

import com.miage.miageland_back.dto.EmployeeDTO;
import com.miage.miageland_back.entities.Employee;
import com.miage.miageland_back.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("miageland/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping()
    public void getEmployee(@RequestBody EmployeeDTO employeeDTO, HttpServletResponse response) {
        employeeService.loginEmployee(employeeDTO.getEmail(),response);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void postEmployee(@RequestBody Employee employee,
                             @CookieValue(value = "manager") Cookie managerCookie) {
        if (!employeeService.isEmployee(managerCookie.getValue()))
            throw new EntityNotFoundException("Logged in employee does not exists");
        employeeService.createEmployee(employee);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployee(@RequestBody Employee employee) {
        //todo : only admin can delete employee, so add annotation to check the user is an admin
        employeeService.deleteEmployee(employee.getEmail());
    }
}
