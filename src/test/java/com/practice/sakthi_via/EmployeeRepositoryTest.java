package com.practice.sakthi_via;

import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.repository.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EmployeeRepositoryTest {
    @Autowired
    EmployeeRepository employeeRepository;
    Employee employee = new Employee();

    @Before
    public void setup() {
        employee.setName("Sakthi");
        employee.setEmail("sgsakthi1992@gmail.com");
        employee.setUsername("sgsakthi");
        employee.setAge(27);
        employeeRepository.save(employee);
    }

    @Test
    public void whenValidEmail_thenEmployeeShouldBeFound() {
        Optional<List> employeeList = employeeRepository.findByEmail(employee.getEmail());
        assertTrue(employeeList.isPresent());
    }

    @Test
    public void whenValidUsername_thenEmployeeShouldBeFound() {
        Employee foundEmployee = employeeRepository.findByUsername(employee.getUsername());
        assertEquals(foundEmployee.getUsername(), employee.getUsername());
    }

    @Test
    public void whenUpdateEmail_thenOldEmailShouldNotBeFound() {
        employeeRepository.updateEmployeeEmail(employee.getId(), "newemail@gmail.com");
        Optional<List> employeeList = employeeRepository.findByEmail(employee.getEmail());
        assertFalse(employeeList.isPresent());
    }

}
