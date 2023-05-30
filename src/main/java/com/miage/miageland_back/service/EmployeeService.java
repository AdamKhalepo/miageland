package com.miage.miageland_back.service;

import com.miage.miageland_back.dao.repository.EmployeeRepository;
import com.miage.miageland_back.entities.Employee;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CookieService cookieService;

    private boolean requiredFields(Employee employee) {
        return (employee.getEmail() == null || employee.getName() == null
                || employee.getFirstName() == null || employee.getRole() == null);
    }

    public void createEmployee(Employee newEmployee) {
        if (requiredFields(newEmployee))
            throw new IllegalArgumentException("Missing parameters, please provide all parameters");

        if(employeeRepository.existsByEmail(newEmployee.getEmail())) {
            throw new EntityExistsException("Employee already exists");
        } else {
            System.out.println(newEmployee.getRole());
            employeeRepository.save(newEmployee);
        }
    }

    public void loginEmployee(String employeeEmail, HttpServletResponse response) {
        if (!employeeRepository.existsByEmail(employeeEmail))
            throw new EntityNotFoundException("Employee does not exist");

        Employee loggedEmployee = employeeRepository.findByEmail(employeeEmail);

        cookieService.deleteCookies(response);
        //Adding cookie to response to keep track of the employee
        //This cookie needs to be sent back to the server to identify the employee
        cookieService.addEmployeeCookie(loggedEmployee,response);
    }

    public void deleteEmployee(String employeeEmail) {
        if (!employeeRepository.existsByEmail(employeeEmail))
            throw new EntityNotFoundException("Employee does not exist");
        employeeRepository.deleteByEmail(employeeEmail);
    }

    public boolean isEmployee(String employeeEmail) {
        return employeeRepository.existsByEmail(employeeEmail);
    }
}
