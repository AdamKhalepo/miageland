package com.miage.miageland_back.controller;

import com.miage.miageland_back.entities.Employee;
import com.miage.miageland_back.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public void getEmployee(@RequestBody Employee employee) {
        //TODO: do we need to handle sessions (spring security) ? or use cookies in headers ?
        employeeService.loginEmployee(employee.getEmail());
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    //todo : only admin can create employee, so add annotation to check the user is an admin
    public void postEmployee(@RequestBody Employee employee) {
        employeeService.createEmployee(employee);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployee(@RequestBody Employee employee) {
        //todo : only admin can delete employee, so add annotation to check the user is an admin
        employeeService.deleteEmployee(employee.getEmail());
    }
}
