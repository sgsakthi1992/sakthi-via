package com.practice.sakthi_via.facade;

import com.practice.sakthi_via.constants.Constants;
import com.practice.sakthi_via.exception.ResourceNotFoundException;
import com.practice.sakthi_via.mail.EmailService;
import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.model.dto.EmployeeDto;
import com.practice.sakthi_via.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

/**
 * Employee service class.
 *
 * @author Sakthi_Subramaniam
 */
@Service
public class EmployeeFacade {

    /**
     * EmployeeRepository object.
     */
    private EmployeeRepository employeeRepository;
    /**
     * EmailService object.
     */
    private EmailService emailService;

    /**
     * Setter for EmployeeRepository object.
     *
     * @param employeeRepository EmployeeRepository object
     */
    @Autowired
    public void setEmployeeRepository(
            final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Setter for EmailService object.
     *
     * @param emailService EmailService object
     */
    @Autowired
    public void setEmailService(final EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Logger Object to log the details.
     */
    private static final Logger LOGGER = LoggerFactory.
            getLogger(EmployeeFacade.class);

    /**
     * Method to check username already exists.
     *
     * @param userName Employee username
     * @return true or false
     */
    public boolean checkUsername(final String userName) {
        Employee employee = employeeRepository.findByUsername(userName);
        if (employee != null) {
            LOGGER.debug("Username {} exists", userName);
            return false;
        }
        return true;
    }

    /**
     * To convert EmployeeDto to Employee.
     *
     * @param employeeDto Employee details
     * @return Employee
     */
    public Employee convertEmployeeDtoToEmployee(
            final EmployeeDto employeeDto) {
        ModelMapper modelMapper = new ModelMapper();
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        LOGGER.debug("Mapped details: {}", employee);
        return employee;
    }

    /**
     * To create new employee.
     *
     * @param employeeDto Employee details
     * @return Employee
     */
    public Employee createEmployee(final EmployeeDto employeeDto) {
        Employee employee = convertEmployeeDtoToEmployee(employeeDto);
        employeeRepository.save(employee);
        LOGGER.debug("Created Employee: {}", employee);
        emailService.sendMail(employee.getEmail(),
                "Employee created in SAKTHI-VIA", employee);
        return employee;
    }

    /**
     * To get all the employees.
     *
     * @return List of employees.
     */
    public List<Employee> getEmployees() {
        List<Employee> employeeList = employeeRepository.findAll();
        LOGGER.debug("List of employees : {}", employeeList);
        return employeeList;
    }

    /**
     * To get employee details by id.
     *
     * @param id Employee Id
     * @return Employee
     * @throws ResourceNotFoundException id not found
     */
    public Employee getEmployeeById(final Long id)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> {
                    LOGGER.error(Constants.EMPLOYEE_ID_NOT_FOUND_MSG, id);
                    return new ResourceNotFoundException(
                            Constants.EMPLOYEE_ID_NOT_FOUND);
                });
        LOGGER.debug("Employee details: {}", employee);
        return employee;
    }

    /**
     * To delete employee by id.
     *
     * @param id Employee Id
     * @return String
     * @throws ResourceNotFoundException id not found
     */
    public String deleteEmployeeById(final Long id)
            throws ResourceNotFoundException {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
        LOGGER.debug("Delete success");
        return "Success";
    }

    /**
     * To get Employee details by email.
     *
     * @param email Employee email
     * @return List of Employees
     * @throws ResourceNotFoundException email not found
     */
    public List getEmployeeByEmail(final String email)
            throws ResourceNotFoundException {
        return employeeRepository.findByEmail(email).orElseThrow(
                () -> {
                    LOGGER.error(Constants.EMPLOYEE_EMAIL_NOT_FOUND_MSG, email);
                    return new ResourceNotFoundException(
                            Constants.EMPLOYEE_EMAIL_NOT_FOUND);
                });
    }

    /**
     * To get Employee details either by Username or email.
     *
     * @param username Employee username
     * @param email    Employee email
     * @return List of Employees
     * @throws ResourceNotFoundException Username & email not found
     */
    public List<Employee> getEmployeeByUsernameOrEmail(
            final String username, final String email)
            throws ResourceNotFoundException {
        if (username == null && email == null) {
            throw new ResourceNotFoundException(
                    Constants.EMPLOYEE_USERNAME_OR_EMAIL_NOT_FOUND);
        }
        final Example<Employee> example = getExample(username, email);
        List<Employee> employeeList = employeeRepository.findAll(example);
        if (employeeList.isEmpty()) {
            LOGGER.error(Constants.EMPLOYEE_USERNAME_OR_EMAIL_NOT_FOUND_MSG,
                    username, email);
            throw new ResourceNotFoundException(
                    Constants.EMPLOYEE_USERNAME_OR_EMAIL_NOT_FOUND);
        }
        LOGGER.debug("Employee list: {}", employeeList);
        return employeeList;
    }

    /**
     * Get Example.
     *
     * @param username Employee username
     * @param email    Employee email
     * @return Example of Employee
     */
    public Example<Employee> getExample(final String username,
                                        final String email) {
        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setEmail(email);
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("email", contains().ignoreCase())
                .withMatcher("username", contains().ignoreCase());
        Example<Employee> example = Example.of(employee, exampleMatcher);
        LOGGER.debug("Example Employee: {}", example.getProbe());
        return example;
    }

    /**
     * To Update employee email.
     *
     * @param id    Employee id
     * @param email Employee email
     * @return String
     * @throws ResourceNotFoundException id not found
     */
    public String updateEmployeeEmail(final Long id, final String email)
            throws ResourceNotFoundException {
        Employee employee = getEmployeeById(id);
        employeeRepository.updateEmployeeEmail(employee.getId(), email);
        return "Success";
    }

}
