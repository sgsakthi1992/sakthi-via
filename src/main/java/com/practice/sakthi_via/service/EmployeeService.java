package com.practice.sakthi_via.service;

import com.practice.sakthi_via.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;
    public boolean checkUsername(String username){
        return employeeRepository.findByUsername(username);
    }
}
