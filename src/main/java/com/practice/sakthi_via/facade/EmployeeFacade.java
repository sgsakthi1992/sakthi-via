package com.practice.sakthi_via.facade;

import com.practice.sakthi_via.exception.ResourceNotFoundException;
import com.practice.sakthi_via.mail.EmailService;
import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.model.Mail;
import com.practice.sakthi_via.model.RatesRegister;
import com.practice.sakthi_via.model.dto.EmployeeDto;
import com.practice.sakthi_via.model.dto.RatesRegisterDto;
import com.practice.sakthi_via.repository.EmployeeRepository;
import com.practice.sakthi_via.repository.RatesRegisterRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Service
@CacheConfig(cacheNames = "employeeCache")
public class EmployeeFacade {
    /**
     * Logger Object to log the details.
     */
    private static final Logger LOGGER = LoggerFactory.
            getLogger(EmployeeFacade.class);
    /**
     * Message for Employee Id not found.
     */
    private static final String EMPLOYEE_ID_NOT_FOUND = "Employee Id not found";
    /**
     * Message for Employee Username or Email not found.
     */
    private static final String EMPLOYEE_USERNAME_OR_EMAIL_NOT_FOUND
            = "Employee Username or email id not found";
    /**
     * Message for Employee Email not found.
     */
    private static final String EMPLOYEE_EMAIL_NOT_FOUND
            = "Employee email not found";
    /**
     * Success message.
     */
    private static final String SUCCESS_MESSAGE = "Success";
    /**
     * Email Subject.
     */
    private static final String EMAIL_SUBJECT = "Welcome to SAKTHI-VIA!!";
    /**
     * EmployeeRepository object.
     */
    private EmployeeRepository employeeRepository;
    /**
     * EmailService object.
     */
    private EmailService emailService;
    /**
     * RatesRegisterRepository object.
     */
    private RatesRegisterRepository registerRepository;
    /**
     * Model mapper.
     */
    private ModelMapper modelMapper;

    /**
     * Default constructor.
     */
    public EmployeeFacade() {
        modelMapper = new ModelMapper();
    }

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
     * Setter for RatesRegisterRepository object.
     *
     * @param registerRepository RatesRegisterRepository object
     */
    @Autowired
    public void setRegisterRepository(
            final RatesRegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
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
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        LOGGER.debug("Mapped details: {}", employee);
        return employee;
    }

    /**
     * To convert RatesRegisterDto to RatesRegister.
     *
     * @param ratesRegisterDto RatesRegister details
     * @return RatesRegisterDto
     * @throws ResourceNotFoundException exception
     */
    public RatesRegister convertRatesRegisterDtoToRatesRegister(
            final RatesRegisterDto ratesRegisterDto)
            throws ResourceNotFoundException {
        RatesRegister ratesRegister = modelMapper.map(ratesRegisterDto,
                RatesRegister.class);
        Employee employee = employeeRepository
                .findById(ratesRegisterDto.getId()).orElseThrow(() -> new
                        ResourceNotFoundException("Not a valid Employee ID"));
        ratesRegister.setEmployee(employee);
        LOGGER.debug("Mapped details: {}", ratesRegister);
        return ratesRegister;
    }

    /**
     * To create new employee.
     *
     * @param employeeDto Employee details
     * @return Employee
     * @throws MessagingException exception
     */
    public Employee createEmployee(final EmployeeDto employeeDto)
            throws MessagingException {
        Employee employee = convertEmployeeDtoToEmployee(employeeDto);
        employeeRepository.save(employee);
        LOGGER.debug("Created Employee: {}", employee);
        Map<String, Object> content = new HashMap<>();
        content.put("name", employee.getName());
        content.put("username", employee.getUsername());
        content.put("age", employee.getAge());
        content.put("email", employee.getEmail());

        Mail mail = new Mail(employee.getEmail(),
                EMAIL_SUBJECT, content, "welcomeMailTemplate");
        emailService.sendMail(mail);
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
    @Cacheable(cacheNames = "employeeCache")
    public Employee getEmployeeById(final Long id)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> {
                    LOGGER.error("Employee Id {} not found", id);
                    return new ResourceNotFoundException(
                            EMPLOYEE_ID_NOT_FOUND);
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
    @CacheEvict(cacheNames = "employeeCache", key = "#id")
    public String deleteEmployeeById(final Long id)
            throws ResourceNotFoundException {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
        LOGGER.debug("Delete success");
        return SUCCESS_MESSAGE;
    }

    /**
     * To get Employee details by email.
     *
     * @param email Employee email
     * @return List of Employees
     * @throws ResourceNotFoundException email not found
     */
    @Cacheable(cacheNames = "employeeCache", key = "#email")
    public List<Employee> getEmployeeByEmail(final String email)
            throws ResourceNotFoundException {
        return employeeRepository.findByEmail(email).orElseThrow(
                () -> {
                    LOGGER.error("Email {} not found", email);
                    return new ResourceNotFoundException(
                            EMPLOYEE_EMAIL_NOT_FOUND);
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
                    EMPLOYEE_USERNAME_OR_EMAIL_NOT_FOUND);
        }
        final Example<Employee> example = getExample(username, email);
        List<Employee> employeeList = employeeRepository.findAll(example);
        if (employeeList.isEmpty()) {
            LOGGER.error("Employee not found with Username: {} or Email: {}",
                    username, email);
            throw new ResourceNotFoundException(
                    EMPLOYEE_USERNAME_OR_EMAIL_NOT_FOUND);
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
    @Caching(evict = {
            @CacheEvict(cacheNames = "employeeCache", key = "#id"),
            @CacheEvict(cacheNames = "employeeCache", key = "#email")
    })
    public String updateEmployeeEmail(final Long id, final String email)
            throws ResourceNotFoundException {
        Employee employee = getEmployeeById(id);
        employeeRepository.updateEmployeeEmail(employee.getId(), email);
        return SUCCESS_MESSAGE;
    }

    /**
     * To register for rated feed mail.
     *
     * @param ratesRegisterDto rated register details
     * @return registration success message
     * @throws ResourceNotFoundException exception
     */
    public String registerForRates(final RatesRegisterDto ratesRegisterDto)
            throws ResourceNotFoundException {
        RatesRegister ratesRegister = convertRatesRegisterDtoToRatesRegister(
                ratesRegisterDto);

        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
                .withMatcher("employee_id", contains().ignoreCase())
                .withMatcher("base", contains().ignoreCase());
        Example<RatesRegister> example = Example
                .of(ratesRegister, exampleMatcher);
        Optional<RatesRegister> register = registerRepository.findOne(example);

        register.ifPresentOrElse(record -> {
            LOGGER.debug("Combination available, updating");
            ratesRegister.getTarget().forEach(
                    record.getTarget()::add);
            registerRepository.save(record);
        }, () -> {
            LOGGER.debug("New record");
            registerRepository.save(ratesRegister);
        });
        return SUCCESS_MESSAGE;
    }

}
