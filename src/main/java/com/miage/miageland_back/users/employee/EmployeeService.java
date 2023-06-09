package com.miage.miageland_back.users.employee;

import com.miage.miageland_back.security.CookieService;
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

    private boolean missingFields(Employee employee) {
        return (employee.getEmail() == null || employee.getName() == null
                || employee.getFirstName() == null || employee.getRole() == null);
    }

    public EmployeeDTO createEmployee(Employee newEmployee) {
        if (missingFields(newEmployee))
            throw new IllegalArgumentException("Missing parameters, please provide all parameters");

        if(this.employeeRepository.existsByEmail(newEmployee.getEmail())) {
            throw new EntityExistsException("Employee already exists");
        } else {
            this.employeeRepository.save(newEmployee);

            return new EmployeeDTO(newEmployee.getId(), newEmployee.getEmail(), newEmployee.getRole());
        }
    }

    public void loginEmployee(String employeeEmail, HttpServletResponse response) {
        if (!this.employeeRepository.existsByEmail(employeeEmail))
            throw new EntityNotFoundException("Employee does not exist");

        cookieService.deleteUserCookie(response);
        //Adding cookie to response to keep track of the employee
        //This cookie needs to be sent back to the server to identify the employee
        cookieService.addUserCookie(employeeEmail,response);
    }

    public void deleteEmployee(Long employeeIdToDelete, String userEmail) {
        if (!this.employeeRepository.existsById(employeeIdToDelete))
            throw new EntityNotFoundException("Employee does not exist.");

        Employee connectedEmployee = this.employeeRepository.findByEmail(userEmail);
        if (employeeIdToDelete.equals(connectedEmployee.getId()))
            throw new IllegalStateException("You cannot delete yourself.");

        this.employeeRepository.deleteById(employeeIdToDelete);
    }

    public boolean isEmployee(String email) {
        return this.employeeRepository.existsByEmail(email);
    }

    public boolean isManager(String email) {
        return this.employeeRepository.existsByEmailAndRole(email, EmployeeRole.MANAGER);
    }

    public boolean isAdmin(String email) {
        return this.employeeRepository.existsByEmailAndRole(email, EmployeeRole.ADMIN);
    }
}
