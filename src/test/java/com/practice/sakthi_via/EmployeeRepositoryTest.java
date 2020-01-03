package com.practice.sakthi_via;

import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.repository.EmployeeRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.AfterTestClass;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integration-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;

    private static final String NAME = "Employee 4";
    private static final String EMAIL = "employee4@gmail.com";
    private static final String USERNAME = "employee4";
    private static final Integer AGE = 27;

    @Test
    @Order(1)
    public void testSaveEmployee() {
        Employee employee = new Employee();
        employee.setName(NAME);
        employee.setEmail(EMAIL);
        employee.setUsername(USERNAME);
        employee.setAge(AGE);
        employeeRepository.save(employee);
        System.out.println(employee);
    }

    @Test
    @Order(2)
    public void whenValidUsername_thenEmployeeShouldBeFound() {
        Employee foundEmployee = employeeRepository.findByUsername(USERNAME);
        System.out.println(foundEmployee);
        assertEquals(foundEmployee.getUsername(), USERNAME);
    }

    @Test
    @Order(3)
    public void whenValidEmail_thenEmployeeShouldBeFound() {
        Optional<List> employeeList = employeeRepository.findByEmail(EMAIL);
        System.out.println(employeeList);
        assertTrue(employeeList.isPresent());
    }

    @Test
    @Order(4)
    public void whenUpdateEmail_thenOldEmailShouldNotBeFound() {
        Employee employee = employeeRepository.findByUsername(USERNAME);
        employeeRepository.updateEmployeeEmail(employee.getId(), "newemail@gmail.com");
        Optional<List> employeeList = employeeRepository.findByEmail(EMAIL);
        assertFalse(employeeList.isPresent());
    }

}
