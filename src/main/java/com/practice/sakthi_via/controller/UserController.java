package com.practice.sakthi_via.controller;

import com.practice.sakthi_via.exception.ResourceNotFoundException;
import com.practice.sakthi_via.model.Users;
import com.practice.sakthi_via.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getUsers")
    public ResponseEntity<List> getUsers() {
        List<Users> usersList = userRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }

    @PostMapping("/createUser")
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        userRepository.save(user);
        Users newUser = userRepository.findById(user.getId()).get();
        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }

    @GetMapping(value = "/getUserById/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable(value = "id") Integer id) throws ResourceNotFoundException {
        Users user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User ID " + id + " not found"));
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/deleteUserById/{id}")
    public ResponseEntity<String> deleteById(@PathVariable(value = "id") Integer id) throws ResourceNotFoundException {
        Users user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User ID " + id + " not found"));
        userRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<List> getUserById(@PathVariable(value = "email") String email) throws ResourceNotFoundException {
        List<Users> usersList = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email " + email + " not found"));
        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }

    @GetMapping("/getUserByUsernameOrEmail")
    public ResponseEntity<List> getUserByUsernameOrEmail(Users user) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("email", contains().ignoreCase())
                .withMatcher("username", contains().ignoreCase());
        Example<Users> example = Example.of(user, exampleMatcher);

        List<Users> usersList = userRepository.findAll(example);
        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }

    @ApiOperation(value = "Update an employee")
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<Users> updateUser(
            @ApiParam(value = "User Id to update user object", required = true) @PathVariable(value = "id") Integer id,
            @ApiParam(value = "Update User object", required = true) @Valid @RequestBody Users userDetails) throws ResourceNotFoundException {
        Users user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User ID " + id + " not found"));
        Users updatedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

}
