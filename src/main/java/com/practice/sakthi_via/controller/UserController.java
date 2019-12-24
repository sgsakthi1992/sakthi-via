package com.practice.sakthi_via.controller;

import com.practice.sakthi_via.exception.ResourceNotFoundException;
import com.practice.sakthi_via.model.Users;
import com.practice.sakthi_via.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/getUsers", method = RequestMethod.GET)
    public ResponseEntity<List> getUsers() {
        List<Users> usersList = userRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        userRepository.save(user);
        Users newUser = userRepository.findById(user.getId()).get();
        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }

    @RequestMapping(value = "/getUserById/{id}", method = RequestMethod.GET)
    public ResponseEntity<Users> getUserById(@PathVariable(value = "id") Integer id) throws ResourceNotFoundException {
        Users user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Employee ID " + id + " not found"));
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @RequestMapping(value = "/deleteUserById/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> deleteById(@PathVariable(value = "id") Integer id) throws ResourceNotFoundException {
        Users user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Employee ID " + id + " not found"));
        userRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }
}
