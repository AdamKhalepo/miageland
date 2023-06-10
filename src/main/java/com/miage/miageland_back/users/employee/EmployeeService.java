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

    /**
     * Check if the employee has all the required fields
     * @param employee the employee to check
     * @return true if the employee has all the required fields, false otherwise
     */
    private boolean missingFields(Employee employee) {
        return (employee.getEmail() == null || employee.getName() == null
                || employee.getFirstName() == null || employee.getRole() == null);
    }

    /**
     * Create an employee
     * @param newEmployee the employee to create
     * @return the created employee
     */
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

    /**
     * Login the employee
     * @param employeeEmail the email of the employee to login
     * @param response the response to set the cookie in the header
     */
    public void loginEmployee(String employeeEmail, HttpServletResponse response) {
        cookieService.deleteUserCookie(response);
        //Adding cookie to response to keep track of the employee
        //This cookie needs to be sent back to the server to identify the employee
        cookieService.addUserCookie(employeeEmail,response);
    }

    /**
     * Delete an employee
     * @param employeeIdToDelete the id of the employee to delete
     * @param userEmail the email of the user (set in the cookie)
     */
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
