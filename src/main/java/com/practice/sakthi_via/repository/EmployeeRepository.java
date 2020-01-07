package com.practice.sakthi_via.repository;

import com.practice.sakthi_via.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface.
 *
 * @author Sakthi_Subramaniam
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    /**
     * To retrieve Employee details by email.
     *
     * @param email
     * @return List of employees
     */
    Optional<List> findByEmail(String email);

    /**
     * To retrieve Employee details by username.
     *
     * @param username
     * @return Employee
     */
    Employee findByUsername(String username);

    /**
     * To Update employee email based on id.
     *
     * @param id
     * @param email
     * @return 1 or 0
     */
    @Transactional
    @Modifying
    @Query("UPDATE Employee e SET e.email = :email where e.id = :id")
    Integer updateEmployeeEmail(@Param("id") Long id,
                                @Param("email") String email);
}
