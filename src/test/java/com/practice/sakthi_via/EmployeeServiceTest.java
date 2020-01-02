package com.practice.sakthi_via;

import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.repository.EmployeeRepository;
import com.practice.sakthi_via.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class EmployeeServiceTest {

    @TestConfiguration
    static class EmployeeServiceTestContextConfiguration {

        @Bean
        public EmployeeService employeeService() {
            return new EmployeeService();
        }
    }

    @Autowired
    EmployeeService employeeService;

    @MockBean
    EmployeeRepository employeeRepository;

    @Before
    public void setup() {
        Employee employee;
        employee = new Employee();
        employee.setUsername("sgsakthi");
        Mockito.when(employeeRepository.findByUsername(Mockito.any(String.class))).thenReturn(null);
        Mockito.when(employeeRepository.findByUsername(employee.getUsername()))
                .thenReturn(employee);
    }

    @Test
    public void whenValidUserName_thenReturnTrue() {
        assertTrue(employeeService.checkUsername("validusername"));
    }

    @Test
    public void whenInValidUserName_thenReturnTrue() {
        assertFalse(employeeService.checkUsername("sgsakthi"));
    }
}
