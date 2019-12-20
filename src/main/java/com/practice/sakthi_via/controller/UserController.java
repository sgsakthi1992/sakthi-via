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
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/getUsers", method = RequestMethod.GET)
    public ResponseEntity<List> getUsers() {
        Users user = new Users(1, "Gokula Sakthi", "sakthi", "sgsakthi1992@gmail.com");
        Users user1 = new Users(2, "Mallikarjun", "malli", "mallikarjun.bandi@gmail.com");
        userRepository.save(user);
		userRepository.save(user1);
        List<Users> usersList = userRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }
}
