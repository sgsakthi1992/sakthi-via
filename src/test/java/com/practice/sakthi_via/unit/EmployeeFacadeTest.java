package com.practice.sakthi_via.unit;

import com.practice.sakthi_via.exception.ResourceNotFoundException;
import com.practice.sakthi_via.facade.EmployeeFacade;
import com.practice.sakthi_via.mail.EmailService;
import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.model.Mail;
import com.practice.sakthi_via.model.dto.EmployeeDto;
import com.practice.sakthi_via.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.MessagingException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class EmployeeFacadeTest {

    @InjectMocks
    EmployeeFacade employeeFacade;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    EmailService emailService;

    @Spy
    @InjectMocks
    EmployeeFacade spyEmployeeFacade;

    Employee employee;

    EmployeeFacadeTest() {
        employee = new Employee((long) 40000, "Employee 1",
                "employee1", "emp1@gmail.com", 25);
    }

    @Test
    void testCheckUserName() {
        //GIVEN
        when(employeeRepository.findByUsername(anyString())).thenReturn(null);
        when(employeeRepository.findByUsername(employee.getUsername()))
                .thenReturn(employee);
        //WHEN
        boolean isValidUsername = employeeFacade.checkUsername("validUsername");
        boolean isInvalidUsername = employeeFacade.checkUsername(employee.getUsername());

        //THEN
        assertTrue(isValidUsername);
        assertFalse(isInvalidUsername);
    }

    @Test
    void testConvertEmployeeDtoToEmployee() {
        //GIVEN
        EmployeeDto employeeDto = new EmployeeDto("Employee 1",
                "employee1", "emp1@gmail.com", 25);
        //WHEN
        Employee convertedEmployee = employeeFacade.convertEmployeeDtoToEmployee(employeeDto);

        //THEN
        assertEquals(employeeDto.getName(), convertedEmployee.getName());
        assertEquals(employeeDto.getUsername(), convertedEmployee.getUsername());
        assertEquals(employeeDto.getEmail(), convertedEmployee.getEmail());
        assertEquals(employeeDto.getAge(), convertedEmployee.getAge());
    }

    @Test
    void testCreateEmployee() throws MessagingException {
        //GIVEN
        EmployeeDto employeeDto = new EmployeeDto("Employee 1",
                "employee1", "emp1@gmail.com", 25);
        ArgumentCaptor<Mail> captor = ArgumentCaptor.forClass(Mail.class);

        when(spyEmployeeFacade.convertEmployeeDtoToEmployee(employeeDto)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        doNothing().when(emailService).sendMail(any(Mail.class));

        //WHEN
        Employee createdEmployee = spyEmployeeFacade.createEmployee(employeeDto);

        //THEN
        verify(emailService).sendMail(captor.capture());
        assertEquals(employee.getEmail(), captor.getValue().getTo());
        assertEquals(employee.getName(), createdEmployee.getName());
        assertEquals(employee.getUsername(), createdEmployee.getUsername());
        assertEquals(employee.getEmail(), createdEmployee.getEmail());
        assertEquals(employee.getAge(), createdEmployee.getAge());
        assertEquals(employee.getId(), createdEmployee.getId());
    }

    @Test
    void testFindAllEmployees() {
        //GIVEN
        when(employeeRepository.findAll()).thenReturn(Stream.of(employee).collect(Collectors.toList()));
        //WHEN
        List<Employee> employees = employeeFacade.getEmployees();
        //THEN
        assertEquals(1, employees.size());
        assertEquals(employee.getName(), employees.get(0).getName());
    }

    @Test
    void testFindEmployeeById() throws ResourceNotFoundException {
        //GIVEN
        when(employeeRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());
        when(employeeRepository.findById(employee.getId())).thenReturn(java.util.Optional.of(employee));
        //WHEN
        Employee employeeByValidId = employeeFacade.getEmployeeById(employee.getId());
        //THEN
        assertEquals(employee.getName(), employeeByValidId.getName());
        assertThrows(ResourceNotFoundException.class, () -> employeeFacade.getEmployeeById((long) 40001));
    }

    @Test
    void testDeleteEmployeeById() throws ResourceNotFoundException {
        //GIVEN
        when(employeeRepository.findById(employee.getId())).thenReturn(java.util.Optional.of(employee));
        Mockito.doNothing().when(employeeRepository).delete(employee);
        //WHEN
        String message = employeeFacade.deleteEmployeeById(employee.getId());
        //THEN
        assertEquals("Success", message);
    }

    @Test
    void testFindEmployeeByEmail() throws ResourceNotFoundException {
        //GIVEN
        when(employeeRepository.findByEmail(anyString())).thenReturn(java.util.Optional.empty());
        when(employeeRepository.findByEmail(employee.getEmail()))
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
    void testUpdateEmployeeEmail() throws ResourceNotFoundException {
        //GIVEN
        when(employeeRepository.findById(employee.getId())).thenReturn(java.util.Optional.of(employee));
        when(employeeRepository.updateEmployeeEmail(employee.getId(), employee.getEmail())).thenReturn(1);
        //WHEN
        String message = employeeFacade.updateEmployeeEmail(employee.getId(), employee.getEmail());
        //THEN
        assertEquals("Success", message);
    }

    @Test
    void testGetExample() {
        //GIVEN
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
    void testGetEmployeeByUsernameOrEmail() throws ResourceNotFoundException {
        //GIVEN
        Example<Employee> example = employeeFacade.getExample(employee.getUsername(), employee.getEmail());
        Mockito.doReturn(example).when(spyEmployeeFacade).getExample(employee.getUsername(), employee.getEmail());
        Mockito.doReturn(example).when(spyEmployeeFacade).getExample(employee.getUsername(), null);
        Mockito.doReturn(example).when(spyEmployeeFacade).getExample(null, employee.getEmail());
        when(employeeRepository.findAll(example))
                .thenReturn(Stream.of(employee).collect(Collectors.toList()));

        //WHEN
        List<Employee> employeeByUsernameOrEmail = spyEmployeeFacade
                .getEmployeeByUsernameOrEmail(employee.getUsername(), employee.getEmail());
        
        //THEN
        assertEquals(1, employeeByUsernameOrEmail.size());
        assertThrows(ResourceNotFoundException.class, ()
                -> spyEmployeeFacade.getEmployeeByUsernameOrEmail("new", "new")).printStackTrace();
        assertThrows(ResourceNotFoundException.class, ()
                -> spyEmployeeFacade.getEmployeeByUsernameOrEmail(null, null));
        assertDoesNotThrow(() -> spyEmployeeFacade.getEmployeeByUsernameOrEmail(employee.getUsername(), null));
        assertDoesNotThrow(() -> spyEmployeeFacade.getEmployeeByUsernameOrEmail(null, employee.getEmail()));
    }
}
