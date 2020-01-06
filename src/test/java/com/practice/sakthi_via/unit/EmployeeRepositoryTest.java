package com.practice.sakthi_via.unit;

import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.repository.EmployeeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;

    private static final String NAME = "Employee 4";
    private static final String EMAIL = "employee4@gmail.com";
    private static final String USERNAME = "employee4";
    private static final Integer AGE = 27;

    @BeforeEach
    public void setup() {
        Employee employee = new Employee();
        employee.setName(NAME);
        employee.setEmail(EMAIL);
        employee.setUsername(USERNAME);
        employee.setAge(AGE);
        employeeRepository.save(employee);
    }

    @Test
    public void testFindByUsername() {
        //GIVEN
        //WHEN
        Employee foundEmployee = employeeRepository.findByUsername(USERNAME);
        //THEN
        assertEquals(USERNAME, foundEmployee.getUsername());
    }

    @Test
    public void testFindByEmail() {
        //GIVEN
        //WHEN
        Optional<List> employeeList = employeeRepository.findByEmail(EMAIL);
        //THEN
        assertTrue(employeeList.isPresent());
    }

    @Test
    public void testUpdateEmail() {
        //GIVEN
        Employee employee = employeeRepository.findByUsername(USERNAME);
        employeeRepository.updateEmployeeEmail(employee.getId(), "newemail@gmail.com");
        //WHEN
        Optional<List> employeeList = employeeRepository.findByEmail(EMAIL);
        //THEN
        assertFalse(employeeList.isPresent());
    }

}