package com.practice.employee.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.practice.employee.facade.EmployeeFacade;
import com.practice.employee.model.Employee;
import com.practice.employee.model.RatesRegister;
import com.practice.employee.model.dto.EmployeeDto;
import com.practice.employee.model.dto.RatesRegisterDto;
import com.practice.employee.repository.EmployeeRepository;
import com.practice.employee.repository.RatesRegisterRepository;
import com.practice.employee.service.OtpService;
import com.practice.exception.ResourceNotFoundException;
import com.practice.message.factory.AbstractFactory;
import com.practice.message.model.Content;
import com.practice.message.service.MessagingService;
import com.practice.message.service.impl.EmailService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;

@ExtendWith(MockitoExtension.class)
class EmployeeFacadeTest {

  @InjectMocks
  EmployeeFacade employeeFacade;

  @Mock
  EmployeeRepository employeeRepository;

  @Mock
  RatesRegisterRepository ratesRegisterRepository;

  @Mock
  AbstractFactory<MessagingService> abstractFactory;

  @Mock
  ModelMapper modelMapper;

  @Mock
  OtpService otpService;

  @Mock
  EmailService emailService;

  private Employee employee;

  EmployeeFacadeTest() {
    employee = new Employee((long) 40000, "Employee 1",
        "employee1", "emp1@gmail.com", "+111111111", 25);
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
        "employee1", "emp1@gmail.com", "+111111111", 25);
    when(modelMapper.map(employeeDto, Employee.class)).thenReturn(employee);
    //WHEN
    Employee convertedEmployee = employeeFacade.convertEmployeeDtoToEmployee(employeeDto);

    //THEN
    assertEquals(employeeDto.getName(), convertedEmployee.getName());
    assertEquals(employeeDto.getUsername(), convertedEmployee.getUsername());
    assertEquals(employeeDto.getEmail(), convertedEmployee.getEmail());
    assertEquals(employeeDto.getAge(), convertedEmployee.getAge());
  }

  @Test
  void testConvertRatesRegisterDtoToRatesRegister() throws ResourceNotFoundException {
    //GIVEN
    RatesRegisterDto ratesRegisterDto = new RatesRegisterDto(40000L,
        "HUF", Set.of("INR", "EUR"), 10000);
    when(modelMapper.map(ratesRegisterDto, RatesRegister.class)).thenReturn(
        new RatesRegister(1, employee, "HUF", Set.of("INR", "EUR")));
    when(employeeRepository.findById(ratesRegisterDto.getId()))
        .thenReturn(Optional.ofNullable(employee));
    //WHEN
    RatesRegister ratesRegister = employeeFacade
        .convertRatesRegisterDtoToRatesRegister(ratesRegisterDto);

    //THEN
    assertEquals("HUF", ratesRegister.getBase());
    assertEquals(Set.of("INR", "EUR"), ratesRegister.getTarget());
    assertEquals(employee.getId(), ratesRegister.getEmployee().getId());
  }

  @Test
  void testConvertRatesRegisterDtoToRatesRegisterWithNewId() {
    //GIVEN
    RatesRegisterDto ratesRegisterDto = new RatesRegisterDto(40000L,
        "HUF", Set.of("INR", "EUR"), 10000);
    //WHEN
    //THEN
    assertThrows(ResourceNotFoundException.class,
        () -> employeeFacade.convertRatesRegisterDtoToRatesRegister(ratesRegisterDto));
  }

  @Test
  void testCreateEmployee() {
    //GIVEN
    EmployeeDto employeeDto = new EmployeeDto("Employee 1",
        "employee1", "emp1@gmail.com", "+111111111", 25);
    ArgumentCaptor<Content> captor = ArgumentCaptor.forClass(Content.class);

    //when(spyEmployeeFacade.convertEmployeeDtoToEmployee(employeeDto)).thenReturn(employee);
    when(modelMapper.map(employeeDto, Employee.class)).thenReturn(employee);
    when(employeeRepository.save(employee)).thenReturn(employee);
    when(abstractFactory.create("email"))
        .thenReturn(emailService);

    //WHEN
    Employee createdEmployee = employeeFacade.createEmployee(employeeDto);

    //THEN
    verify(abstractFactory.create("email")).send(captor.capture());
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
    assertThat(employees, hasSize(1));
    assertEquals(employee.getName(), employees.get(0).getName());
  }

  @Test
  void testFindEmployeeById() throws ResourceNotFoundException {
    //GIVEN
    when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
    when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
    //WHEN
    Employee employeeByValidId = employeeFacade.getEmployeeById(employee.getId());
    //THEN
    assertEquals(employee.getName(), employeeByValidId.getName());
    assertThrows(ResourceNotFoundException.class, () -> employeeFacade.getEmployeeById((long) 40001));
  }

  @Test
  void testDeleteEmployeeById() throws ResourceNotFoundException {
    //GIVEN
    when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
    Mockito.doNothing().when(employeeRepository).delete(employee);
    //WHEN
    String message = employeeFacade.deleteEmployeeById(employee.getId());
    //THEN
    assertEquals("Success", message);
  }

  @Test
  void testFindEmployeeByEmail() throws ResourceNotFoundException {
    //GIVEN
    when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.empty());
    when(employeeRepository.findByEmail(employee.getEmail()))
        .thenReturn(Optional.of(
            Stream.of(employee).collect(Collectors.toList())));
    //WHEN
    List<Employee> employeesByValidEmail = employeeFacade.getEmployeeByEmail(employee.getEmail());
    //THEN
    assertThat(employeesByValidEmail, hasSize(1));
    assertEquals(employee.getName(), employeesByValidEmail.get(0).getName());
    assertThrows(ResourceNotFoundException.class, () -> employeeFacade.getEmployeeByEmail("newemail@gmail.com"));
  }

  @Test
  void testUpdateEmployeeEmail() throws ResourceNotFoundException {
    //GIVEN
    when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
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
    when(employeeRepository.findAll(any(Example.class)))
        .thenReturn(Stream.of(employee).collect(Collectors.toList()));

    //WHEN
    List<Employee> employeeByUsernameOrEmail = employeeFacade
        .getEmployeeByUsernameOrEmail(employee.getUsername(), employee.getEmail());

    //THEN
    assertThat(employeeByUsernameOrEmail, hasSize(1));
    assertThrows(ResourceNotFoundException.class, ()
        -> employeeFacade.getEmployeeByUsernameOrEmail(null, null));
  }

  @Test
  void registerForRates() throws ResourceNotFoundException {
    //GIVEN
    RatesRegisterDto ratesRegisterDto = new RatesRegisterDto(40000L,
        "HUF", Set.of("INR", "EUR"), 10000);
    when(modelMapper.map(ratesRegisterDto, RatesRegister.class)).thenReturn(
        new RatesRegister(1, employee, "HUF", Set.of("INR", "EUR")));
    when(employeeRepository.findById(ratesRegisterDto.getId()))
        .thenReturn(Optional.ofNullable(employee));
    when(ratesRegisterRepository.findOne(any(Example.class))).thenReturn(Optional.empty());
    when(otpService.getOtp(ratesRegisterDto.getId())).thenReturn(10000);
    //WHEN
    String message = employeeFacade.registerForRates(ratesRegisterDto);
    //THEN
    assertEquals("Success", message);
  }

  @Test
  void registerForRatesWithExistingBase() throws ResourceNotFoundException {
    //GIVEN
    RatesRegisterDto ratesRegisterDto = new RatesRegisterDto(40000L,
        "HUF", Set.of("INR", "EUR"), 10000);

    Set<String> target = new HashSet<>();
    target.add("USD");

    RatesRegister register = new RatesRegister(1, employee, "HUF", target);
    when(modelMapper.map(ratesRegisterDto, RatesRegister.class)).thenReturn(
        new RatesRegister(1, employee, "HUF", Set.of("INR", "EUR")));
    when(employeeRepository.findById(ratesRegisterDto.getId()))
        .thenReturn(Optional.ofNullable(employee));
    when(ratesRegisterRepository.findOne(any(Example.class)))
        .thenReturn(Optional.of(register));
    when(otpService.getOtp(ratesRegisterDto.getId())).thenReturn(10000);
    //WHEN
    String message = employeeFacade.registerForRates(ratesRegisterDto);
    //THEN
    assertEquals("Success", message);
    assertTrue(register.getTarget().contains("INR"));
    assertTrue(register.getTarget().contains("EUR"));
  }
}
