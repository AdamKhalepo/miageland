package com.miage.miageland_back.users.employee;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("miageland/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/login")
    public void loginEmployee(@RequestBody Employee employee, HttpServletResponse response) {
        this.employeeService.loginEmployee(employee.getEmail(),response);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO postEmployee(@RequestBody Employee employee,
                             @CookieValue(value = "user") Cookie managerCookie) throws IllegalAccessException {
        if (!this.employeeService.isManager(managerCookie.getValue()))
            throw new IllegalAccessException("You must be a manager to call this endpoint.");
        return this.employeeService.createEmployee(employee);
    }

    @DeleteMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployee(@PathVariable Long employeeId,
                               @CookieValue(value = "user") Cookie managerCookie) throws IllegalAccessException {
        if (!this.employeeService.isManager(managerCookie.getValue()))
            throw new IllegalAccessException("You must be a manager to call this endpoint.");
        this.employeeService.deleteEmployee(employeeId,managerCookie.getValue());
    }
}
