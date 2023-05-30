package com.miage.miageland_back.service;

import com.miage.miageland_back.dao.repository.EmployeeRepository;
import com.miage.miageland_back.entities.Employee;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public void createEmployee(Employee newEmployee) {
        if(employeeRepository.existsByEmail(newEmployee.getEmail())) {
            throw new EntityExistsException("Employee already exists");
        } else {
            System.out.println(newEmployee.getRole());
            employeeRepository.save(newEmployee);
        }
    }

    public void loginEmployee(String employeeEmail) {
        if (!employeeRepository.existsByEmail(employeeEmail))
            throw new EntityNotFoundException("Employee does not exist");
        //TODO : else create cookie or use spring security ?
    }

    public void deleteEmployee(String employeeEmail) {
        if (!employeeRepository.existsByEmail(employeeEmail))
            throw new EntityNotFoundException("Employee does not exist");
        employeeRepository.deleteByEmail(employeeEmail);
    }
}
