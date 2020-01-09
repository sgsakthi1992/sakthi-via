package com.practice.sakthi_via.unit;

import com.practice.sakthi_via.exception.ResourceNotFoundException;
import com.practice.sakthi_via.facade.EmployeeFacade;
import com.practice.sakthi_via.mail.EmailService;
import com.practice.sakthi_via.mail.impl.EmailServiceImpl;
import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.model.dto.EmployeeDto;
import com.practice.sakthi_via.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.MessagingException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class EmployeeFacadeTest {

    @TestConfiguration
    static class EmployeeFacadeTestConfiguraion {
        @Bean
        public EmployeeFacade employeeFacade() {
            return new EmployeeFacade();
        }

        @Bean
        public EmailService emailService() {
            return new EmailServiceImpl();
        }

        @Bean
        public JavaMailSender javaMailSender() {
            return new JavaMailSenderImpl();
        }
    }

    @Autowired
    EmployeeFacade employeeFacade;

    @MockBean
    EmployeeRepository employeeRepository;

    @MockBean
    EmailService emailService;

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
    public void testCreateEmployee() throws MessagingException {
        //GIVEN
        EmployeeDto employeeDto = new EmployeeDto("Employee 1",
                "employee1", "emp1@gmail.com", 25);
        Employee employee = new Employee((long) 40000, "Employee 1",
                "employee1", "emp1@gmail.com", 25);
        EmployeeFacade spyEmployeeFacade = Mockito.spy(employeeFacade);
        Mockito.when(spyEmployeeFacade.convertEmployeeDtoToEmployee(employeeDto)).thenReturn(employee);
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);
        //WHEN
        Employee createdEmployee = spyEmployeeFacade.createEmployee(employeeDto);
        //THEN
        assertEquals(employee.getName(), createdEmployee.getName());
        assertEquals(employee.getUsername(), createdEmployee.getUsername());
        assertEquals(employee.getEmail(), createdEmployee.getEmail());
        assertEquals(employee.getAge(), createdEmployee.getAge());
        assertEquals(employee.getId(), createdEmployee.getId());
    }

    @Test
    public void testFindAllEmployees() {
        //GIVEN
        Employee employee = new Employee((long) 40000, "Employee 1",
                "employee1", "emp1@gmail.com", 25);
        Mockito.when(employeeRepository.findAll()).thenReturn(Stream.of(employee).collect(Collectors.toList()));
        //WHEN
        List<Employee> employees = employeeFacade.getEmployees();
        //THEN
        assertEquals(1, employees.size());
        assertEquals(employee.getName(), employees.get(0).getName());
    }

    @Test
    public void testFindEmployeeById() throws ResourceNotFoundException {
        //GIVEN
        Employee employee = new Employee((long) 40000, "Employee 1",
                "employee1", "emp1@gmail.com", 25);
        Mockito.when(employeeRepository.findById(Mockito.any(Long.class))).thenReturn(java.util.Optional.empty());
        Mockito.when(employeeRepository.findById(employee.getId())).thenReturn(java.util.Optional.of(employee));
        //WHEN
        Employee employeeByValidId = employeeFacade.getEmployeeById(employee.getId());
        //THEN
        assertEquals(employee.getName(), employeeByValidId.getName());
        assertThrows(ResourceNotFoundException.class, () -> employeeFacade.getEmployeeById((long) 40001));
    }

    @Test
    public void testDeleteEmployeeById() throws ResourceNotFoundException {
        //GIVEN
        Employee employee = new Employee((long) 40000, "Employee 1",
                "employee1", "emp1@gmail.com", 25);
        Mockito.when(employeeRepository.findById(employee.getId())).thenReturn(java.util.Optional.of(employee));
        Mockito.doNothing().when(employeeRepository).delete(employee);
        //WHEN
        String message = employeeFacade.deleteEmployeeById(employee.getId());
        //THEN
        assertEquals("Success", message);
    }

    @Test
    public void testFindEmployeeByEmail() throws ResourceNotFoundException {
        //GIVEN
        Employee employee = new Employee((long) 40000, "Employee 1",
                "employee1", "emp1@gmail.com", 25);
        Mockito.when(employeeRepository.findByEmail(Mockito.any(String.class))).thenReturn(java.util.Optional.empty());
        Mockito.when(employeeRepository.findByEmail(employee.getEmail()))
                .thenReturn(java.util.Optional.of(
                        Stream.of(employee).collect(Collectors.toList())));
        //WHEN
        List<Employee> employeesByValidEmail = employeeFacade.getEmployeeByEmail(employee.getEmail());
        //THEN
        assertEquals(1, employeesByValidEmail.size());
        assertEquals(employee.getName(), employeesByValidEmail.get(0).getName());
        assertThrows(ResourceNotFoundException.class, () -> employeeFacade.getEmployeeByEmail("newemail@gmail.com"));
    }

    @Test
    public void testUpdateEmployeeEmail() throws ResourceNotFoundException {
        //GIVEN
        Employee employee = new Employee((long) 40000, "Employee 1",
                "employee1", "emp1@gmail.com", 25);
        Mockito.when(employeeRepository.findById(employee.getId())).thenReturn(java.util.Optional.of(employee));
        Mockito.when(employeeRepository.updateEmployeeEmail(employee.getId(), employee.getEmail())).thenReturn(1);
        //WHEN
        String message = employeeFacade.updateEmployeeEmail(employee.getId(), employee.getEmail());
        //THEN
        assertEquals("Success", message);
    }

    @Test
    public void testGetExample() {
        //GIVEN
        Employee employee = new Employee((long) 40000, "Employee 1",
                "employee1", "emp1@gmail.com", 25);
        //WHEN
        Example<Employee> example = employeeFacade.getExample(employee.getUsername(), employee.getEmail());
        //THEN
        assertNotNull(example.getProbe().getUsername());
        assertNotNull(example.getProbe().getEmail());
        assertNull(example.getProbe().getName());
        assertNull(example.getProbe().getId());
        assertNull(example.getProbe().getAge());
    }

    @Test
    public void testGetEmployeeByUsernameOrEmail() throws ResourceNotFoundException {
        //GIVEN
        Employee employee = new Employee((long) 40000, "Employee 1",
                "employee1", "emp1@gmail.com", 25);
        Example<Employee> example = employeeFacade.getExample(employee.getUsername(), employee.getEmail());
        EmployeeFacade spyEmployeeFacade = Mockito.spy(employeeFacade);
        Mockito.doReturn(example).when(spyEmployeeFacade).getExample(employee.getUsername(), employee.getEmail());
        Mockito.when(employeeRepository.findAll(example))
                .thenReturn(Stream.of(employee).collect(Collectors.toList()));
        //WHEN
        List<Employee> employeeByUsernameOrEmail = spyEmployeeFacade
                .getEmployeeByUsernameOrEmail(employee.getUsername(), employee.getEmail());
        //THEN
        assertEquals(1, employeeByUsernameOrEmail.size());
    }
}
