package com.practice.sakthi_via.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.practice.sakthi_via.model.Users;

@Controller
public class UserController {

	@RequestMapping(value = "/getUsers", method = RequestMethod.GET)
	public ResponseEntity<Users> getUsers() {
		Users user = new Users(1, "Gokula Sakthi", "sakthi", "sgsakthi1992@gmail.com");
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
}
