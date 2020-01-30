package com.practice.web.unit;

import com.practice.employee.facade.EmployeeFacade;
import com.practice.employee.model.Employee;
import com.practice.employee.model.dto.EmployeeDto;
import com.practice.employee.model.dto.RatesRegisterDto;
import com.practice.exception.ResourceNotFoundException;
import com.practice.web.controller.EmployeeController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    private static final String EMPLOYEE_ID_NOT_FOUND = "Employee Id not found";
    private static final String EMPLOYEE_EMAIL_NOT_FOUND = "Employee email not found";
    private static final String EMPLOYEE_USERNAME_OR_EMAIL_NOT_FOUND = "Employee Username or email id not found";

    @InjectMocks
    EmployeeController employeeController;

    @Mock
    EmployeeFacade employeeFacade;

    private Employee employee;

    EmployeeControllerTest() {
        employee = new Employee();
        employee.setId((long) 40000);
        employee.setName("Sakthi");
        employee.setEmail("sgsakthi1992@gmail.com");
        employee.setUsername("sgsakthi");
        employee.setAge(27);
    }

    @Test
    @DisplayName("Fetch Employee")
    void testGetEmployee() {
        //GIVEN
        when(employeeFacade.getEmployees()).thenReturn(
                Stream.of(employee).collect(Collectors.toList()));

        //WHEN
        ResponseEntity<List> responseEntity = employeeController.getEmployees();
        //THEN
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.hasBody());
        assertEquals(employee.toString(), Objects.requireNonNull(responseEntity.getBody()).get(0).toString());
    }

    @Test
    @DisplayName("Create Employee with valid details")
    void testPostEmployee() throws Exception {
        //GIVEN
        EmployeeDto employeeDto = new EmployeeDto("Employee 1",
                "employee1", "emp1@gmail.com", 25);

        when(employeeFacade.createEmployee(Mockito.any(EmployeeDto.class))).thenReturn(employee);

        //WHEN
        ResponseEntity<Employee> responseEntity = employeeController.createEmployee(employeeDto);

        //THEN
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.hasBody());
        assertEquals(employee.toString(), Objects.requireNonNull(responseEntity.getBody()).toString());
    }

    @Test
    @DisplayName("Fetch Employee with valid Id")
    void testGetEmployeeById() throws Exception {
        //GIVEN
        when(employeeFacade.getEmployeeById(employee.getId())).thenReturn(employee);

        //WHEN
        ResponseEntity<Employee> responseEntity = employeeController.getEmployeeById(employee.getId());

        //THEN
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.hasBody());
        assertEquals(employee.toString(), Objects.requireNonNull(responseEntity.getBody()).toString());
    }

    @Test
    @DisplayName("Fetch Employee with invalid Id")
    void testGetEmployeeWithInvalidId() throws Exception {
        //GIVEN
        Long id = 1L;
        when(employeeFacade.getEmployeeById(id)).thenThrow(
                new ResourceNotFoundException(EMPLOYEE_ID_NOT_FOUND));

        //WHEN
        //THEN
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> employeeController.getEmployeeById(id));
        assertTrue(exception.getMessage().contains(EMPLOYEE_ID_NOT_FOUND));

    }

    @Test
    @DisplayName("Delete Employee with Id")
    void testDeleteEmployeeById() throws Exception {
        //GIVEN
        when(employeeFacade.deleteEmployeeById(employee.getId())).thenReturn("Success");

        //WHEN
        ResponseEntity<String> responseEntity = employeeController.deleteEmployeeById(employee.getId());

        //THEN
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.hasBody());
        assertEquals("Success", responseEntity.getBody());
    }

    @Test
    @DisplayName("Delete Employee with Invalid Id")
    void testDeleteEmployeeWithInvalidId() throws Exception {
        //GIVEN
        Long id = 1L;
        when(employeeFacade.deleteEmployeeById(id)).thenThrow(
                new ResourceNotFoundException(EMPLOYEE_ID_NOT_FOUND));

        //WHEN
        //THEN
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> employeeController.deleteEmployeeById(id));
        assertTrue(exception.getMessage().contains(EMPLOYEE_ID_NOT_FOUND));
    }

    @Test
    @DisplayName("Update Employee Email with Id")
    void testUpdateEmployeeEmailById() throws Exception {
        //GIVEN
        when(employeeFacade.updateEmployeeEmail(employee.getId(), "newemail@gmail.com")).thenReturn("Success");

        //WHEN
        ResponseEntity<String> responseEntity = employeeController.updateEmployeeEmail(employee.getId(), "newemail@gmail.com");

        //THEN
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.hasBody());
        assertEquals("Success", responseEntity.getBody());
    }

    @Test
    @DisplayName("Update Employee Email with invalid Id")
    void testUpdateEmployeeEmailWithInvalidId() throws Exception {
        //GIVEN
        Long id = 1L;
        when(employeeFacade.updateEmployeeEmail(id, "newemail@gmail.com"))
                .thenThrow(new ResourceNotFoundException(EMPLOYEE_ID_NOT_FOUND));

        //WHEN
        //THEN
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> employeeController.updateEmployeeEmail(id, "newemail@gmail.com"));
        assertTrue(exception.getMessage().contains(EMPLOYEE_ID_NOT_FOUND));
    }

    @Test
    @DisplayName("Fetch Employee with Email")
    void testGetEmployeeByEmail() throws Exception {
        //GIVEN
        when(employeeFacade.getEmployeeByEmail(employee.getEmail()))
                .thenReturn(Stream.of(employee).collect(Collectors.toList()));

        //WHEN
        ResponseEntity<List> responseEntity = employeeController.getEmployeeByEmail(employee.getEmail());

        //THEN
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.hasBody());
        assertEquals(employee.toString(), Objects.requireNonNull(responseEntity.getBody()).get(0).toString());

    }

    @Test
    @DisplayName("Fetch Employee with not available Email")
    void testGetEmployeeByEmailWithNewEmail() throws Exception {
        //GIVEN
        String email = "newemail@gmail.com";
        when(employeeFacade.getEmployeeByEmail(email)).thenThrow(
                new ResourceNotFoundException(EMPLOYEE_EMAIL_NOT_FOUND));

        //WHEN
        //THEN
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> employeeController.getEmployeeByEmail(email));
        assertTrue(exception.getMessage().contains(EMPLOYEE_EMAIL_NOT_FOUND));
    }


    @Test
    @DisplayName("Fetch Employee with Username or Email")
    void testGetEmployeeByUsernameOrEmail() throws Exception {
        //GIVEN
        when(employeeFacade.getEmployeeByUsernameOrEmail(employee.getUsername(), employee.getEmail()))
                .thenReturn(Stream.of(employee).collect(Collectors.toList()));

        //WHEN
        ResponseEntity<List> responseEntity = employeeController.getEmployeeByUsernameOrEmail(employee.getUsername(), employee.getEmail());

        //THEN
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.hasBody());
        assertEquals(employee.toString(), Objects.requireNonNull(responseEntity.getBody()).get(0).toString());
    }

    @Test
    @DisplayName("Fetch Employee with Username or Email with new Email & Username")
    void testGetEmployeeByUsernameOrEmailWithNewEmailAndUserName() throws ResourceNotFoundException {
        //GIVEN
        String email = "newemail@gmail.com";
        String username = "username";
        when(employeeFacade.getEmployeeByUsernameOrEmail(username, email))
                .thenThrow(new ResourceNotFoundException(EMPLOYEE_USERNAME_OR_EMAIL_NOT_FOUND));
        //WHEN
        //THEN
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> employeeController.getEmployeeByUsernameOrEmail(username, email));
        assertTrue(exception.getMessage().contains(EMPLOYEE_USERNAME_OR_EMAIL_NOT_FOUND));
    }

    @Test
    void registerForRates() throws ResourceNotFoundException {
        //GIVEN
        RatesRegisterDto ratesRegisterDto = new RatesRegisterDto(1L, "HUF", Set.of("INR","USD"));
        when(employeeFacade.registerForRates(ratesRegisterDto)).thenReturn("Success");
        //WHEN
        ResponseEntity<String> responseEntity = employeeController.registerForRates(ratesRegisterDto);
        //THEN
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.hasBody());
        assertEquals("Success", responseEntity.getBody());
    }

}
