package com.practice.sakthi_via;

import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.repository.EmployeeRepository;
import com.practice.sakthi_via.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {

    @TestConfiguration
    static class EmployeeServiceTestConfiguration{
        @Bean
        public EmployeeService employeeService(){
            return new EmployeeService();
        }

    }
    @Autowired
    EmployeeService employeeService;

    @MockBean
    EmployeeRepository employeeRepository;

    @BeforeEach
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
