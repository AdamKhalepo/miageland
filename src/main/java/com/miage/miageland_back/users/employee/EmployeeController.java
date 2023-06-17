package com.miage.miageland_back.users.employee;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.web.server.ResponseStatusException;

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
        try {
            if (!employeeService.isEmployee(employee.getEmail()))
                throw new IllegalAccessException("Incorrect user, try with another one.");

            this.employeeService.loginEmployee(employee.getEmail(), response);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    /**
     * Create a new employee (you must be logged as a manager to call this endpoint)
     * @param employee the employee to create
     * @param managerEmail the email of the manager (set in the cookie)
     * @return the created employee
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO postEmployee(@RequestBody Employee employee,
                             @CookieValue(value = "user") String managerEmail) {
        try {
            if (!this.employeeService.isManager(managerEmail))
                throw new IllegalAccessException("You must be a manager to call this endpoint.");
            return this.employeeService.createEmployee(employee);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    /**
     * Delete an employee (you must be logged as a manager to call this endpoint)
     * @param employeeId the id of the employee to delete
     * @param managerCookie the cookie of the manager (set in the cookie)
     */
    @DeleteMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployee(@PathVariable Long employeeId,
                               @CookieValue(value = "user") Cookie managerCookie) {
        try {
            if (!this.employeeService.isManager(managerCookie.getValue()))
                throw new IllegalAccessException("You must be a manager to call this endpoint.");
            this.employeeService.deleteEmployee(employeeId, managerCookie.getValue());
        } catch (IllegalAccessException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
