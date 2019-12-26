package com.practice.sakthi_via.repository;

import com.practice.sakthi_via.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

   Optional<List> findByEmail(String email);

   Boolean findByUsername(String username);
}
