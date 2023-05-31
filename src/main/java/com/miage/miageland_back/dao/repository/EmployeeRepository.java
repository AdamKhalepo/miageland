package com.miage.miageland_back.dao.repository;

import com.miage.miageland_back.enums.EmployeeRole;
import com.miage.miageland_back.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    Employee findByEmail(String employeeEmail);

    boolean existsByEmailAndRole(String email, EmployeeRole role);
}
