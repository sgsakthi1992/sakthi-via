package com.practice.sakthi_via.validator;

import com.practice.sakthi_via.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UsernameValidator implements ConstraintValidator<Username, String> {

    @Autowired
    EmployeeService employeeService;

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext constraintValidatorContext) {
        return !employeeService.checkUsername(userName);
    }
}
