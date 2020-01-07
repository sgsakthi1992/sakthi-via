package com.practice.sakthi_via.validator;

import com.practice.sakthi_via.facade.EmployeeFacade;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Username Validator class.
 *
 * @author Sakthi_Subramaniam
 */
public class UsernameValidator
        implements ConstraintValidator<Username, String> {
    /**
     * EmployeeService object.
     */
    private EmployeeFacade employeeFacade;

    /**
     * Setter for Employee service object.
     * @param employeeFacade
     */
    @Autowired
    public void setEmployeeFacade(final EmployeeFacade employeeFacade) {
        this.employeeFacade = employeeFacade;
    }

    /**
     * Overridden isValid method.
     * @param userName
     * @param context
     * @return true or false
     */
    @Override
    public boolean isValid(final String userName,
                           final ConstraintValidatorContext context) {
        return employeeFacade.checkUsername(userName);
    }
}
