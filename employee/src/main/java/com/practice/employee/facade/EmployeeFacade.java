package com.practice.employee.facade;

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
import com.practice.message.model.OtpDetails;
import com.practice.message.service.MessagingService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

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
     * Welcome Email Subject.
     */
    private static final String EMAIL_SUBJECT = "Welcome to SAKTHI-VIA!!";
    /**
     * Otp Email Subject.
     */
    private static final String OTP_EMAIL_SUBJECT =
            "One Time Password (OTP) on Sakthi-VIA";
    /**
     * Scheduler mail template.
     */
    private static final String MAIL_TEMPLATE = "welcomeMailTemplate";
    /**
     * Otp mail template.
     */
    private static final String OTP_MAIL_TEMPLATE = "otpMailTemplate";
    /**
     * Messaging type email.
     */
    private static final String TYPE_EMAIL = "email";
    /**
     * Messaging type sms.
     */
    private static final String TYPE_SMS = "sms";
    /**
     * EmployeeRepository object.
     */
    private final EmployeeRepository employeeRepository;
    /**
     * RatesRegisterRepository object.
     */
    private final RatesRegisterRepository registerRepository;
    /**
     * Model mapper.
     */
    private final ModelMapper modelMapper;
    /**
     * Messaging Service object.
     */
    private final AbstractFactory<MessagingService> abstractFactory;
    /**
     * OtpService object.
     */
    private final OtpService otpService;

    /**
     * Parameterized Constructor.
     *
     * @param employeeRepository EmployeeRepository object
     * @param registerRepository RatesRegisterRepository object
     * @param modelMapper        ModelMapper object
     * @param abstractFactory    Abstract Factory of type Messaging Service
     * @param otpService         Otp Service object
     */
    public EmployeeFacade(final EmployeeRepository employeeRepository,
                          final RatesRegisterRepository registerRepository,
                          final ModelMapper modelMapper,
                          final AbstractFactory<MessagingService>
                                  abstractFactory,
                          final OtpService otpService) {
        this.employeeRepository = employeeRepository;
        this.registerRepository = registerRepository;
        this.modelMapper = modelMapper;
        this.abstractFactory = abstractFactory;
        this.otpService = otpService;
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
     */
    public Employee createEmployee(final EmployeeDto employeeDto) {
        Employee employee = convertEmployeeDtoToEmployee(employeeDto);
        employeeRepository.save(employee);
        LOGGER.debug("Created Employee: {}", employee);
        sendMessage(TYPE_EMAIL,
                getContentByType(employee, getWelcomeMailBody(employee),
                        TYPE_EMAIL, EMAIL_SUBJECT, MAIL_TEMPLATE));

        return employee;
    }

    private Map<String, Object> getWelcomeMailBody(
            final Employee employee) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", employee.getName());
        body.put("username", employee.getUsername());
        body.put("age", employee.getAge());
        body.put("email", employee.getEmail());
        return body;
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
        RatesRegister ratesRegister =
                convertRatesRegisterDtoToRatesRegister(
                        ratesRegisterDto);
        if (validateOtp(ratesRegisterDto)) {
            ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
                    .withMatcher("employee_id", contains().ignoreCase())
                    .withMatcher("base", contains().ignoreCase());
            Example<RatesRegister> example = Example
                    .of(ratesRegister, exampleMatcher);
            Optional<RatesRegister> register = registerRepository
                    .findOne(example);

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
        } else {
            LOGGER.error("Not a valid Otp");
            throw new ResourceNotFoundException(
                    "Not a valid Otp. Please generate the Otp & try again.");
        }
    }

    private boolean validateOtp(final RatesRegisterDto ratesRegisterDto) {
        Object otp = otpService.getOtp(ratesRegisterDto.getId());
        if (!otp.equals(0) && otp.equals(ratesRegisterDto.getOtp())) {
            otpService.clearOTP(ratesRegisterDto.getId());
            return true;
        }
        return false;
    }

    /**
     * To generate Otp.
     *
     * @param id   Employee id
     * @param type email or message.
     * @throws ResourceNotFoundException exception
     */
    public void generateOtp(final Long id, final String type)
            throws ResourceNotFoundException {
        Employee employee = getEmployeeById(id);
        OtpDetails otpDetails = new OtpDetails(otpService.getOtp(id).equals(0)
                ? otpService.generateOTP(id) : otpService.getOtp(id),
                otpService.getOtpStartTime(id),
                otpService.getOtpExpiryTime(id));
        sendMessage(type,
                getContentByType(employee,
                        getOtpMessageBody(otpDetails), type,
                        OTP_EMAIL_SUBJECT, OTP_MAIL_TEMPLATE));
    }

    private void sendMessage(final String type, final Content body) {
        abstractFactory.create(type).send(body);
    }

    private Content getContentByType(final Employee employee,
                                     final Map<String, Object> body,
                                     final String type, final String subject,
                                     final String template) {
        Content content;
        if (type.contentEquals(TYPE_SMS)) {
            content = Content.builder()
                    .setTo(employee.getPhoneNumber())
                    .setBody(body).createMail();
            return content;
        } else if (type.contentEquals(TYPE_EMAIL)) {
            content = Content.builder()
                    .setTo(employee.getEmail())
                    .setBody(body)
                    .setSubject(subject)
                    .setTemplate(template)
                    .createMail();
            return content;
        }
        return null;
    }

    private Map<String, Object> getOtpMessageBody(
            final OtpDetails otpDetails) {
        Map<String, Object> body = new HashMap<>();
        body.put("otp", otpDetails.getOtp());
        body.put("startTime", otpDetails.getOtpStartTime());
        body.put("expiryTime", otpDetails.getOtpExpiryTime());
        LOGGER.debug("body: {}", body);
        return body;
    }
}
