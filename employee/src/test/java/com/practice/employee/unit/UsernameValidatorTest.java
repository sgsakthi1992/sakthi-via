package com.practice.employee.unit;

import com.practice.employee.facade.EmployeeFacade;
import com.practice.employee.validator.UsernameValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UsernameValidatorTest {

    @Mock
    EmployeeFacade employeeFacade;

    @Mock
    ConstraintValidatorContext context;

    @InjectMocks
    UsernameValidator usernameValidator;

    @Test
    void isValid() {
        //GIVEN
        when(employeeFacade.checkUsername(anyString())).thenReturn(true);
        when(employeeFacade.checkUsername("employee1")).thenReturn(false);
        //WHEN
        boolean registeredUsername = usernameValidator.isValid("employee1", context);
        boolean newUsername = usernameValidator.isValid("newUsername", context);
        //THEN
        assertFalse(registeredUsername);
        assertTrue(newUsername);
    }
}