package com.practice.sakthi_via.service;

import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public boolean checkUsername(String userName) {
        Employee employee = employeeRepository.findByUsername(userName);
        if (employee != null) {
            logger.debug("Username {} exists", userName);
            return false;
        }
        return true;
    }
}
