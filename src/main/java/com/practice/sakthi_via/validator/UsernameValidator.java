package com.practice.sakthi_via.validator;

import com.practice.sakthi_via.facade.EmployeeFacade;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator
        implements ConstraintValidator<Username, String> {
    /**
     * EmployeeService object.
     */
    private final EmployeeFacade employeeFacade;

    /**
     * Parameterized constructor to bind EmployeeFacade object.
     *
     * @param employeeFacade EmployeeFacade object
     */
    public UsernameValidator(final EmployeeFacade employeeFacade) {
        this.employeeFacade = employeeFacade;
    }

    /**
     * Overridden isValid method.
     *
     * @param userName Employee username
     * @param context  ConstraintValidatorContext
     * @return true or false
     */
    @Override
    public boolean isValid(final String userName,
                           final ConstraintValidatorContext context) {
        return employeeFacade.checkUsername(userName);
    }
}
