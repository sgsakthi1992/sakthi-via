package com.practice.sakthi_via.unit;

import com.practice.sakthi_via.facade.EmployeeFacade;
import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.model.dto.EmployeeDto;
import com.practice.sakthi_via.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class EmployeeFacadeTest {

    @MockBean
    EmployeeFacade employeeFacade;

    @MockBean
    EmployeeRepository employeeRepository;

    @Test
    public void testCheckUserName() {
        //GIVEN
        Employee employee;
        employee = new Employee();
        employee.setUsername("employee");
        Mockito.when(employeeRepository.findByUsername(Mockito.any(String.class))).thenReturn(null);
        Mockito.when(employeeRepository.findByUsername(employee.getUsername()))
                .thenReturn(employee);
        //WHEN
        Boolean isValidUsername = employeeFacade.checkUsername("validUsername");
        Boolean isInvalidUsername = employeeFacade.checkUsername("employee");

        //THEN
        assertTrue(isValidUsername);
        assertFalse(isInvalidUsername);
    }

    @Test
    public void testConvertEmployeeDtoToEmployee() {
        //GIVEN
        EmployeeDto employeeDto = new EmployeeDto("Employee 1",
                "employee1", "emp1@gmail.com", 25);
        //WHEN
        Employee employee = employeeFacade.convertEmployeeDtoToEmployee(employeeDto);

        //THEN
        assertEquals(employeeDto.getName(), employee.getName());
        assertEquals(employeeDto.getUsername(), employee.getUsername());
        assertEquals(employeeDto.getEmail(), employee.getEmail());
        assertEquals(employeeDto.getAge(), employee.getAge());
    }

    @Test
    public void testCreateEmployee() {
        //GIVEN
        EmployeeDto employeeDto = new EmployeeDto("Employee 1",
                "employee1", "emp1@gmail.com", 25);
        Employee employee = new Employee((long) 40000,"Employee 1",
                "employee1", "emp1@gmail.com", 25);
        Mockito.when(employeeFacade.convertEmployeeDtoToEmployee(employeeDto)).thenReturn(employee);
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);
        //WHEN
        Employee createdEmployee = employeeFacade.createEmployee(employeeDto);
        //THEN
        assertEquals(employeeDto.getName(), createdEmployee.getName());
    }

    @Test
    public void testFindAllEmployees(){
        //GIVEN
        Employee employee = new Employee((long) 40000,"Employee 1",
                "employee1", "emp1@gmail.com", 25);
        Mockito.when(employeeRepository.findAll()).thenReturn(Stream.of(employee).collect(Collectors.toList()));
        //WHEN

    }

}
