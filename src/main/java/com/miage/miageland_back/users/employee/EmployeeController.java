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

    /**
     * Login the employee
     * @param employee the employee to login (email must be set)
     * @param response the response to set the cookie
     */
    @GetMapping("/login")
    public void loginEmployee(@RequestBody Employee employee, HttpServletResponse response) {
        if (!employeeService.isEmployee(employee.getEmail()))
            throw new IllegalArgumentException("Incorrect user, try with another one.");

        this.employeeService.loginEmployee(employee.getEmail(),response);
    }

    /**
     * Create a new employee (you must be logged as a manager to call this endpoint)
     * @param employee the employee to create
     * @param managerEmail the email of the manager (set in the cookie)
     * @return the created employee
     * @throws IllegalAccessException if not logged as a manager
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO postEmployee(@RequestBody Employee employee,
                             @CookieValue(value = "user") String managerEmail) throws IllegalAccessException {
        if (!this.employeeService.isManager(managerEmail))
            throw new IllegalAccessException("You must be a manager to call this endpoint.");
        return this.employeeService.createEmployee(employee);
    }

    /**
     * Delete an employee (you must be logged as a manager to call this endpoint)
     * @param employeeId the id of the employee to delete
     * @param managerCookie the cookie of the manager (set in the cookie)
     * @throws IllegalAccessException if not logged as a manager
     */
    @DeleteMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployee(@PathVariable Long employeeId,
                               @CookieValue(value = "user") Cookie managerCookie) throws IllegalAccessException {
        if (!this.employeeService.isManager(managerCookie.getValue()))
            throw new IllegalAccessException("You must be a manager to call this endpoint.");
        this.employeeService.deleteEmployee(employeeId,managerCookie.getValue());
    }
}
