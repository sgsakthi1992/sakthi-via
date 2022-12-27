package com.practice.employee.unit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.practice.currencyconverter.facade.CurrencyConverterFacade;
import com.practice.employee.validator.TargetCodeValidator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TargetCodeValidatorTest {

  @Mock
  CurrencyConverterFacade currencyConverterFacade;

  @Mock
  ConstraintValidatorContext context;

  @InjectMocks
  TargetCodeValidator targetCodeValidator;

  private Map<String, String> countries;

  TargetCodeValidatorTest() {
    countries = new HashMap<>();
    countries.put("HUF", "Hungarian Forint");
    countries.put("INR", "Indian Rupee");
    countries.put("USD", "US Dollar");
  }

  @Test
  void isValid() {
    //GIVEN
    when(currencyConverterFacade.getCountriesAndCurrencies()).thenReturn(countries);

    //WHEN
    boolean validCodes = targetCodeValidator.isValid(Set.of("HUF", "INR"), context);

    //THEN
    assertTrue(validCodes);
  }

  @Test
  void isValidWithSingleInvalidCode() {
    //GIVEN
    when(currencyConverterFacade.getCountriesAndCurrencies()).thenReturn(countries);

    //WHEN
    boolean singleInvalidCode = targetCodeValidator.isValid(Set.of("HHH", "INR"), context);

    //THEN
    assertFalse(singleInvalidCode);
  }

  @Test
  void isValidWithAllInvalidCodes() {
    //GIVEN
    when(currencyConverterFacade.getCountriesAndCurrencies()).thenReturn(countries);

    //WHEN
    boolean invalidCodes = targetCodeValidator.isValid(Set.of("HHH", "III"), context);

    //THEN
    assertFalse(invalidCodes);
  }
}