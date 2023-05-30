package com.miage.miageland_back.dao.repository;

import com.miage.miageland_back.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    void deleteByEmail(String employeeEmail);
}
