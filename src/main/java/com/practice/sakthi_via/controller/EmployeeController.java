package com.practice.sakthi_via.controller;

import com.practice.sakthi_via.constants.Constants;
import com.practice.sakthi_via.exception.ResourceNotFoundException;
import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.model.dto.EmployeeDto;
import com.practice.sakthi_via.repository.EmployeeRepository;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@RestController
@Validated
@RequestMapping("/api/v1")
@Api("Employee Management System")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @ApiOperation("Retrieve all the employees")
    @ApiResponses({
            @ApiResponse(code = 200, message = Constants.EMPLOYEE_RETRIEVE_SUCCESS)
    })
    @GetMapping("/employees")
    public ResponseEntity<List> getEmployee() {
        List<Employee> employeeList = employeeRepository.findAll();
        logger.debug("List of employees : {}", employeeList);
        return ResponseEntity.status(HttpStatus.OK).body(employeeList);
    }

    @ApiOperation("Create Employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = Constants.EMPLOYEE_CREATE_SUCCESS),
            @ApiResponse(code = 400, message = Constants.INVALID_EMPLOYEE_VALUES)
    })
    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(
            @ApiParam(value = "Employee details", required = true) @Valid @RequestBody EmployeeDto employeeDto) {
        ModelMapper modelMapper = new ModelMapper();
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        employeeRepository.save(employee);
        logger.debug("Created Employee: {}", employee);
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @ApiOperation("Get Employee details by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = Constants.EMPLOYEE_RETRIEVE_SUCCESS),
            @ApiResponse(code = 404, message = Constants.EMPLOYEE_ID_NOT_FOUND)
    })
    @GetMapping(value = "/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @ApiParam(value = "Id to retrieve Employee Details", required = true) @PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> {
                    logger.error(Constants.EMPLOYEE_ID_NOT_FOUND_MSG, id);
                    return new ResourceNotFoundException(Constants.EMPLOYEE_ID_NOT_FOUND);
                });
        logger.debug("Employee details: {}", employee);
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @ApiOperation("Delete Employee by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = Constants.EMPLOYEE_DELETE_SUCCESS),
            @ApiResponse(code = 404, message = Constants.EMPLOYEE_ID_NOT_FOUND)
    })
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<String> deleteEmployeeById(
            @ApiParam(value = "Id to delete Employee details", required = true) @PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> {
                    logger.error(Constants.EMPLOYEE_ID_NOT_FOUND_MSG, id);
                    return new ResourceNotFoundException(Constants.EMPLOYEE_ID_NOT_FOUND);
                });
        employeeRepository.delete(employee);
        logger.debug("Delete success");
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @ApiOperation("Get Employee Details By Email")
    @ApiResponses({
            @ApiResponse(code = 200, message = Constants.EMPLOYEE_RETRIEVE_SUCCESS),
            @ApiResponse(code = 400, message = Constants.EMAIL_VALIDATION_MSG),
            @ApiResponse(code = 404, message = Constants.EMPLOYEE_EMAIL_NOT_FOUND)
    })
    @GetMapping("/employeesByEmail/{email}")
    public ResponseEntity<List> getEmployeeByEmail(
            @ApiParam(value = "Email to retrieve Employee Details", required = true)
            @Email(message = Constants.EMAIL_VALIDATION_MSG) @Valid @PathVariable(value = "email") String email)
            throws ResourceNotFoundException {
        List employeeList = employeeRepository.findByEmail(email).orElseThrow(
                () -> {
                    logger.error(Constants.EMPLOYEE_EMAIL_NOT_FOUND_MSG, email);
                    return new ResourceNotFoundException(Constants.EMPLOYEE_EMAIL_NOT_FOUND);
                });
        return ResponseEntity.status(HttpStatus.OK).body(employeeList);
    }

    @ApiOperation("Get Employee Details either by Email or Username")
    @ApiResponses({
            @ApiResponse(code = 200, message = Constants.EMPLOYEE_RETRIEVE_SUCCESS),
            @ApiResponse(code = 400, message = Constants.EMAIL_VALIDATION_MSG),
            @ApiResponse(code = 404, message = Constants.EMPLOYEE_USERNAME_OR_EMAIL_NOT_FOUND)
    })
    @GetMapping("/employeesByUsernameOrEmail")
    public ResponseEntity<List> getEmployeeByUsernameOrEmail(
            @ApiParam(value = "Email to retrieve Employee Details", required = false)
            @Valid @RequestParam(required = false) String username,
            @ApiParam(value = "Username to retrieve Employee Details", required = false)
            @Email(message = Constants.EMAIL_VALIDATION_MSG) @Valid @RequestParam(required = false) String email)
            throws ResourceNotFoundException {
        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setEmail(email);
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("email", contains().ignoreCase())
                .withMatcher("username", contains().ignoreCase());
        Example<Employee> example = Example.of(employee, exampleMatcher);

        List<Employee> employeeList = employeeRepository.findAll(example);
        if (employeeList.isEmpty()) {
            logger.error(Constants.EMPLOYEE_USERNAME_OR_EMAIL_NOT_FOUND_MSG, username, email);
            throw new ResourceNotFoundException(
                    String.format(Constants.EMPLOYEE_USERNAME_OR_EMAIL_NOT_FOUND, username, email));
        }
        logger.debug("Employee list: {}", employeeList);
        return ResponseEntity.status(HttpStatus.OK).body(employeeList);
    }

    @ApiOperation(value = "Update Employee Email")
    @ApiResponses({
            @ApiResponse(code = 200, message = Constants.EMPLOYEE_EMAIL_UPDATED_SUCCESS),
            @ApiResponse(code = 400, message = Constants.EMAIL_VALIDATION_MSG),
            @ApiResponse(code = 404, message = Constants.EMPLOYEE_ID_NOT_FOUND)
    })
    @PutMapping("/employees/{id}")
    public ResponseEntity<String> updateEmployeeEmail(
            @ApiParam(value = "User Id to update employee email id", required = true)
            @Valid @PathVariable(value = "id") Long id,
            @ApiParam(value = "Email address to update", required = true)
            @Email(message = Constants.EMAIL_VALIDATION_MSG) @Valid @RequestParam String email)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(Constants.EMPLOYEE_ID_NOT_FOUND_MSG, id);
                    return new ResourceNotFoundException(Constants.EMPLOYEE_ID_NOT_FOUND);
                });
        employeeRepository.updateEmployeeEmail(employee.getId(), email);
        logger.debug("Updated employee email successfully");
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

}
