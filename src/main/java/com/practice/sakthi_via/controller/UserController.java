package com.practice.sakthi_via.controller;

import com.practice.sakthi_via.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.practice.sakthi_via.model.Users;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/getUsers", method = RequestMethod.GET)
    public ResponseEntity<List> getUsers() {
        Users user = new Users();
        user.setName("Gokula Sakthi");
        user.setUsername("Sakthi");
        user.setEmail("sgsakthi1992@gmail.com");
        userRepository.save(user);
        List<Users> usersList = userRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }

    @RequestMapping(value = "/createUser", method=RequestMethod.POST)
    public void createUser(){

    }
}
