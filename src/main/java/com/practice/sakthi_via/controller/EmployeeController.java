package com.practice.sakthi_via.controller;

import com.practice.sakthi_via.exception.ResourceNotFoundException;
import com.practice.sakthi_via.exception.UserNameExistsException;
import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.repository.EmployeeRepository;
import com.practice.sakthi_via.service.EmployeeService;
import io.swagger.annotations.*;
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
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@RestController
@Validated
@RequestMapping("/api/v1")
@Api("Employee Management System")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @ApiOperation("Retrieve all the employees")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved employee")
    })
    @GetMapping("/employees")
    public ResponseEntity<List> getEmployee() {
        List<Employee> employeeList = employeeRepository.findAll();
        logger.debug("List of employees :" + employeeList);
        return ResponseEntity.status(HttpStatus.OK).body(employeeList);
    }

    @ApiOperation("Create Employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee created successfully"),
            @ApiResponse(code = 400, message = "Employee details has invalid values"),
            @ApiResponse(code = 403, message = "Username not available")
    })
    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(
            @ApiParam(value = "Employee details", required = true) @Valid @RequestBody Employee employee)
            throws UserNameExistsException {
        if (employeeService.checkUsername(employee.getUsername())) {
            logger.error("Username already exist");
            throw new UserNameExistsException("Username not available");
        }
        employeeRepository.save(employee);
        Employee newEmployee = employeeRepository.findById(employee.getId()).get();
        logger.debug("New Employee details: " + employee.toString());
        return ResponseEntity.status(HttpStatus.OK).body(newEmployee);
    }

    @ApiOperation("Get Employee details by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved employee details"),
            @ApiResponse(code = 404, message = "Employee Id not found")
    })
    @GetMapping(value = "/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @ApiParam(value = "Id to retrieve Employee Details", required = true) @PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> {
                    logger.error("Employee ID " + id + " not found");
                    return new ResourceNotFoundException("Employee ID " + id + " not found");
                });
        logger.debug("Employee details: " + employee.toString());
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @ApiOperation("Delete Employee by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully deleted employee details"),
            @ApiResponse(code = 404, message = "Employee Id not found")
    })
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<String> deleteEmployeeById(
            @ApiParam(value = "Id to delete Employee details", required = true) @PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> {
                    logger.error("Employee ID " + id + " not found");
                    return new ResourceNotFoundException("Employee ID " + id + " not found");
                });
        employeeRepository.deleteById(id);
        logger.debug("Delete success");
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @ApiOperation("Get Employee Details By Email")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved employee details"),
            @ApiResponse(code = 400, message = "Not a valid Email address"),
            @ApiResponse(code = 404, message = "Employee email not found")
    })
    @GetMapping("/employeesByEmail/{email}")
    public ResponseEntity<List> getEmployeeByEmail(
            @ApiParam(value = "Email to retrieve Employee Details", required = true)
            @Email(message = "Not a valid Email address") @PathVariable(value = "email") String email)
            throws ResourceNotFoundException {
        List<Employee> employeeList = employeeRepository.findByEmail(email).orElseThrow(
                () -> {
                    logger.error("Email " + email + " not found");
                    return new ResourceNotFoundException("Email " + email + " not found");
                });
        return ResponseEntity.status(HttpStatus.OK).body(employeeList);
    }

    @ApiOperation("Get Employee Details either by Email or Username")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved employee details"),
            @ApiResponse(code = 400, message = "Not a valid email address"),
            @ApiResponse(code = 404, message = "Username and email id not found")
    })
    @GetMapping("/employeesByUsernameOrEmail")
    public ResponseEntity<List> getEmployeeByUsernameOrEmail(
            @ApiParam(value = "Email to retrieve Employee Details", required = false)
            @Valid @RequestParam(required = false) String username,
            @ApiParam(value = "Username to retrieve Employee Details", required = false)
            @Email(message = "Not a valid Email address") @Valid @RequestParam(required = false) String email)
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
            logger.error("Employee not found with Username: " + username + "or Email: " + email);
            throw new ResourceNotFoundException("Employee not found with Username: " + username + "or Email: " + email);
        }
        logger.debug("Employee list: " + employeeList);
        return ResponseEntity.status(HttpStatus.OK).body(employeeList);
    }

    @ApiOperation(value = "Update Employee Email")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated employee email"),
            @ApiResponse(code = 400, message = "Employee details has invalid values"),
            @ApiResponse(code = 404, message = "Employee Id not found")
    })
    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployeeEmail(
            @ApiParam(value = "User Id to update employee email id", required = true)
            @PathVariable(value = "id") Long id,
            @ApiParam(value = "Email address to update", required = true)
            @Email @RequestParam String email) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Employee ID " + id + " not found");
                    return new ResourceNotFoundException("Employee ID " + id + " not found");
                });
        employee.setEmail(email);
        logger.debug("Updated employee: "+ employee);
        employeeRepository.save(employee);
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

}
