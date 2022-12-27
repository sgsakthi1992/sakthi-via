package com.practice.employee.validator;

import com.practice.currencyconverter.facade.CurrencyConverterFacade;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TargetCodeValidator
    implements ConstraintValidator<TargetCode, Set<String>> {

  /**
   * EmployeeService object.
   */
  private final CurrencyConverterFacade currencyConverterFacade;

  /**
   * Parameterized constructor.
   *
   * @param currencyConverterFacade CurrencyConverterFacade object
   */
  public TargetCodeValidator(
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
