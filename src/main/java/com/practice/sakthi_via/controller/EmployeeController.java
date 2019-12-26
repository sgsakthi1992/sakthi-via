package com.practice.sakthi_via.controller;

import com.practice.sakthi_via.exception.ResourceNotFoundException;
import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.repository.EmployeeRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/getEmployee")
    public ResponseEntity<List> getEmployee() {
        List<Employee> employeeList = employeeRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(employeeList);
    }

    @PostMapping("/createUser")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        employeeRepository.save(employee);
        Employee newEmployee = employeeRepository.findById(employee.getId()).get();
        return ResponseEntity.status(HttpStatus.OK).body(newEmployee);
    }

    @GetMapping(value = "/getUserById/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Integer id)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User ID " + id + " not found"));
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @ApiOperation("Delete User by Id")
    @DeleteMapping("/deleteUserById/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable(value = "id") Integer id)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User ID " + id + " not found"));
        employeeRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @ApiOperation("Get User Details By Email")
    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<List> getEmployeeByEmail(
            @ApiParam(value = "Email to retrieve User Details", required = true) @PathVariable(value = "email") String email)
            throws ResourceNotFoundException {
        List<Employee> employeeList = employeeRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email " + email + " not found"));
        return ResponseEntity.status(HttpStatus.OK).body(employeeList);
    }

    @ApiOperation("Get User Details either by Email or Username")
    @GetMapping("/getUserByUsernameOrEmail")
    public ResponseEntity<List> getEmployeeByUsernameOrEmail(
            @ApiParam(value = "Email to retrieve User Details", required = false) @RequestParam String username,
            @ApiParam(value = "Username to retrieve User Details", required = false) @RequestParam String email) {
        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setEmail(email);
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("email", contains().ignoreCase())
                .withMatcher("username", contains().ignoreCase());
        Example<Employee> example = Example.of(employee, exampleMatcher);

        List<Employee> employeeList = employeeRepository.findAll(example);
        return ResponseEntity.status(HttpStatus.OK).body(employeeList);
    }

    @ApiOperation(value = "Update User Details")
    @PatchMapping("/updateUser/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @ApiParam(value = "User Id to update employee object", required = true) @PathVariable(value = "id") Integer id,
            @ApiParam(value = "Update User object", required = true) @Valid @RequestBody Employee employeeDetails)
            throws ResourceNotFoundException {
        Employee updatedEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee ID " + id + " not found"));
        employeeRepository.save(employeeDetails);
        return ResponseEntity.status(HttpStatus.OK).body(updatedEmployee);
    }

}
