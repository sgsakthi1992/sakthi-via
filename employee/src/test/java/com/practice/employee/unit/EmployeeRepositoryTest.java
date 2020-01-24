package com.practice.employee.unit;

import com.practice.employee.model.Employee;
import com.practice.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final String EMAIL = "employee@gmail.com";
    private static final String USERNAME = "employee";

    @Test
    void testFindByUsername() {
        //GIVEN
        //WHEN
        Employee foundEmployee = employeeRepository.findByUsername(USERNAME);
        //THEN
        assertEquals(USERNAME, foundEmployee.getUsername());
    }

    @Test
    void testFindByEmail() {
        //GIVEN
        //WHEN
        Optional<List<Employee>> employeeList = employeeRepository.findByEmail(EMAIL);
        //THEN
        assertTrue(employeeList.isPresent());
    }

    @Test
    void testUpdateEmail() {
        //GIVEN
        Employee employee = employeeRepository.findByUsername(USERNAME);
        employeeRepository.updateEmployeeEmail(employee.getId(), "newemail@gmail.com");
        //WHEN
        Optional<List<Employee>> employeeList = employeeRepository.findByEmail(EMAIL);
        //THEN
        assertFalse(employeeList.isPresent());
    }

}
