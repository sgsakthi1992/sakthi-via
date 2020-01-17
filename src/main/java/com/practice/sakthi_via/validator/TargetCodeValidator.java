/*
 * Copyright (c) 2020.
 */

package com.practice.sakthi_via.validator;

import com.practice.sakthi_via.facade.CurrencyConverterFacade;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.Set;

public class TargetCodeValidator
        implements ConstraintValidator<TargetCode, Set<String>> {
    /**
     * EmployeeService object.
     */
    private CurrencyConverterFacade currencyConverterFacade;


    /**
     * Setter for CurrencyConverterFacade object.
     *
     * @param currencyConverterFacade CurrencyConverterFacade object.
     */
    @Autowired
    public void setCurrencyConverterFacade(
            final CurrencyConverterFacade currencyConverterFacade) {
        this.currencyConverterFacade = currencyConverterFacade;
    }

    /**
     * Overridden isValid method.
     *
     * @param target  target codes
     * @param context ConstraintValidatorContext
     * @return true or false
     */
    @Override
    public boolean isValid(final Set<String> target,
                           final ConstraintValidatorContext context) {
        Map<String, String> codes = currencyConverterFacade
                .getCountriesAndCurrencies();
        return target.stream().allMatch(codes::containsKey);
    }

}
