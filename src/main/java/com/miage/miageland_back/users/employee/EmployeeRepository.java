package com.miage.miageland_back.users.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    Employee findByEmail(String employeeEmail);

    boolean existsByEmailAndRole(String email, EmployeeRole role);
}
