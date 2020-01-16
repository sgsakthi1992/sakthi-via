package com.practice.sakthi_via.controller;

import com.practice.sakthi_via.exception.ResourceNotFoundException;
import com.practice.sakthi_via.facade.EmployeeFacade;
import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.model.dto.EmployeeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1")
@Api("Employee Management System")
public class EmployeeController {

    /**
     * HTTP Status OK value.
     */
    private static final int HTTP_STATUS_OK = 200;
    /**
     * HTTP Status Bad Request value.
     */
    private static final int HTTP_STATUS_BAD_REQUEST = 400;
    /**
     * HTTP Status Not Found value.
     */
    private static final int HTTP_STATUS_NOT_FOUND = 404;
    /**
     * Message for Employee email update success.
     */
    private static final String EMPLOYEE_EMAIL_UPDATED_SUCCESS
            = "Successfully updated employee email";
    /**
     * Message for Employee create success.
     */
    private static final String EMPLOYEE_CREATE_SUCCESS
            = "Employee created successfully";
    /**
     * Message for Employee retrieve success.
     */
    private static final String EMPLOYEE_RETRIEVE_SUCCESS
            = "Successfully retrieved employee details";
    /**
     * Message for Employee delete success.
     */
    private static final String EMPLOYEE_DELETE_SUCCESS
            = "Successfully deleted employee details";
    /**
     * Message for invalid values in Employee details.
     */
    private static final String INVALID_EMPLOYEE_VALUES
            = "Employee details has invalid values";
    /**
     * Message for Employee Id not found.
     */
    private static final String EMPLOYEE_ID_NOT_FOUND = "Employee Id not found";
    /**
     * Message for failed Email validation.
     */
    private static final String EMAIL_VALIDATION_MSG
            = "Not a well-formed email address";
    /**
     * Message for Employee Email not found.
     */
    private static final String EMPLOYEE_EMAIL_NOT_FOUND
            = "Employee email not found";
    /**
     * Message for Employee Username or Email not found.
     */
    private static final String EMPLOYEE_USERNAME_OR_EMAIL_NOT_FOUND
            = "Employee Username or email id not found";
    /**
     * EmployeeFacade object.
     */
    private EmployeeFacade employeeFacade;

    /**
     * Constructor to bind EmployeeFacade object.
     *
     * @param employeeFacade EmployeeFacade object
     */
    public EmployeeController(final EmployeeFacade employeeFacade) {
        this.employeeFacade = employeeFacade;
    }

    /**
     * API to retrieve all the employees.
     *
     * @return Response Entity with List of Employees in body
     */
    @ApiOperation("Retrieve all the employees")
    @ApiResponses({
            @ApiResponse(code = HTTP_STATUS_OK,
                    message = EMPLOYEE_RETRIEVE_SUCCESS)
    })
    @GetMapping("/employees")
    public ResponseEntity<List> getEmployees() {
        List<Employee> employeeList = employeeFacade.getEmployees();
        return ResponseEntity.status(HttpStatus.OK).body(employeeList);
    }

    /**
     * API to create new employee.
     *
     * @param employeeDto Employee details
     * @return ResponseEntity with Employee details in body
     * @throws MessagingException exception
     */
    @ApiOperation("Create Employee")
    @ApiResponses({
            @ApiResponse(code = HTTP_STATUS_OK,
                    message = EMPLOYEE_CREATE_SUCCESS),
            @ApiResponse(code = HTTP_STATUS_BAD_REQUEST,
                    message = INVALID_EMPLOYEE_VALUES)
    })
    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(
            @ApiParam(value = "Employee details", required = true)
            @Valid @RequestBody final EmployeeDto employeeDto)
            throws MessagingException {
        Employee employee = employeeFacade.createEmployee(employeeDto);
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    /**
     * API to get employee details by id.
     *
     * @param id Employee id
     * @return ResponseEntity with Employee details in body
     * @throws ResourceNotFoundException id not found
     */
    @ApiOperation("Get Employee details by Id")
    @ApiResponses({
            @ApiResponse(code = HTTP_STATUS_OK,
                    message = EMPLOYEE_RETRIEVE_SUCCESS),
            @ApiResponse(code = HTTP_STATUS_NOT_FOUND,
                    message = EMPLOYEE_ID_NOT_FOUND)
    })
    @GetMapping(value = "/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @ApiParam(value = "Id to retrieve Employee Details",
                    required = true)
            @PathVariable(value = "id") final Long id)
            throws ResourceNotFoundException {
        Employee employee = employeeFacade.getEmployeeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    /**
     * API to delete employee details.
     *
     * @param id Employee id
     * @return ResponseEntity with String message in body
     * @throws ResourceNotFoundException id not found
     */
    @ApiOperation("Delete Employee by Id")
    @ApiResponses({
            @ApiResponse(code = HTTP_STATUS_OK,
                    message = EMPLOYEE_DELETE_SUCCESS),
            @ApiResponse(code = HTTP_STATUS_NOT_FOUND,
                    message = EMPLOYEE_ID_NOT_FOUND)
    })
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<String> deleteEmployeeById(
            @ApiParam(value = "Id to delete Employee details", required = true)
            @PathVariable(value = "id") final Long id)
            throws ResourceNotFoundException {
        String message = employeeFacade.deleteEmployeeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    /**
     * API to get employee details by Email.
     *
     * @param email Employee email
     * @return Response Entity with List of Employees in body
     * @throws ResourceNotFoundException email not found
     */
    @ApiOperation("Get Employee Details By Email")
    @ApiResponses({
            @ApiResponse(code = HTTP_STATUS_OK,
                    message = EMPLOYEE_RETRIEVE_SUCCESS),
            @ApiResponse(code = HTTP_STATUS_BAD_REQUEST,
                    message = EMAIL_VALIDATION_MSG),
            @ApiResponse(code = HTTP_STATUS_NOT_FOUND,
                    message = EMPLOYEE_EMAIL_NOT_FOUND)
    })
    @GetMapping("/employeesByEmail/{email}")
    public ResponseEntity<List> getEmployeeByEmail(
            @ApiParam(value = "Email to retrieve Employee Details",
                    required = true)
            @Email(message = EMAIL_VALIDATION_MSG) @Valid
            @PathVariable(value = "email") final String email)
            throws ResourceNotFoundException {
        List employeeList = employeeFacade.getEmployeeByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(employeeList);
    }

    /**
     * API to get employee details either by username or email.
     *
     * @param username Employee username
     * @param email    Employee email
     * @return Response Entity with List of Employees in body
     * @throws ResourceNotFoundException emails & username not found
     */
    @ApiOperation("Get Employee Details either by Email or Username")
    @ApiResponses({
            @ApiResponse(code = HTTP_STATUS_OK,
                    message = EMPLOYEE_RETRIEVE_SUCCESS),
            @ApiResponse(code = HTTP_STATUS_BAD_REQUEST,
                    message = EMAIL_VALIDATION_MSG),
            @ApiResponse(code = HTTP_STATUS_NOT_FOUND,
                    message = EMPLOYEE_USERNAME_OR_EMAIL_NOT_FOUND)
    })
    @GetMapping("/employeesByUsernameOrEmail")
    public ResponseEntity<List> getEmployeeByUsernameOrEmail(
            @ApiParam(value = "Email to retrieve Employee Details")
            @Valid @RequestParam(required = false) final String username,
            @ApiParam(value = "Username to retrieve Employee Details")
            @Email(message = EMAIL_VALIDATION_MSG) @Valid
            @RequestParam(required = false) final String email)
            throws ResourceNotFoundException {
        List<Employee> employeeList = employeeFacade
                .getEmployeeByUsernameOrEmail(username, email);
        return ResponseEntity.status(HttpStatus.OK).body(employeeList);
    }

    /**
     * API to update Employee email.
     *
     * @param id    Employee id
     * @param email Email to update
     * @return ResponseEntity String
     * @throws ResourceNotFoundException id not found
     */
    @ApiOperation(value = "Update Employee Email")
    @ApiResponses({
            @ApiResponse(code = HTTP_STATUS_OK,
                    message = EMPLOYEE_EMAIL_UPDATED_SUCCESS),
            @ApiResponse(code = HTTP_STATUS_BAD_REQUEST,
                    message = EMAIL_VALIDATION_MSG),
            @ApiResponse(code = HTTP_STATUS_NOT_FOUND,
                    message = EMPLOYEE_ID_NOT_FOUND)
    })
    @PutMapping("/employees/{id}")
    public ResponseEntity<String> updateEmployeeEmail(
            @ApiParam(value = "User Id to update employee email id",
                    required = true)
            @Valid @PathVariable(value = "id") final Long id,
            @ApiParam(value = "Email address to update", required = true)
            @Email(message = EMAIL_VALIDATION_MSG)
            @Valid @RequestParam final String email)
            throws ResourceNotFoundException {
        String message = employeeFacade.updateEmployeeEmail(id, email);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}
