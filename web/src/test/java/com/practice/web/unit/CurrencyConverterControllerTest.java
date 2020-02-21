package com.practice.web.unit;

import com.practice.currencyconverter.facade.CurrencyConverterFacade;
import com.practice.currencyconverter.model.CurrencyConverter;
import com.practice.exception.ResourceNotFoundException;
import com.practice.web.controller.CurrencyConverterController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyConverterControllerTest {

    @Mock
    CurrencyConverterFacade currencyConverterFacade;

    @InjectMocks
    CurrencyConverterController currencyConverterController;
    private String base = "HUF";
    private CurrencyConverter converter;
    private HashMap<String, String> countries;

    CurrencyConverterControllerTest() {
        countries = new HashMap<>();
        countries.put("HUF", "Hungarian Forint");
        countries.put("INR", "Indian Rupee");
        countries.put("USD", "US Dollar");

        Map<String, Double> rates = new HashMap<>();
        rates.put("GBP", 0.0025654372);
        rates.put("IDR", 45.60031709);
        rates.put("INR", 0.2357907805);
        rates.put("HUF", 1.0);

        converter = new CurrencyConverter();
        converter.setBase(base);
        converter.setDate(LocalDate.now());
        converter.setRates(rates);
    }

    @Test
    void getCountriesAndCurrencies() {
        //GIVEN
        when(currencyConverterFacade.getCountriesAndCurrencies()).thenReturn(countries);

        //WHEN
        ResponseEntity<Map> responseEntity = currencyConverterController.getCountriesAndCurrencies();

        //THEN
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.hasBody());
        assertNotNull(Objects.requireNonNull(responseEntity.getBody()).get(base));

    }

    @Test
    void getCountryForCurrencyCode() throws ResourceNotFoundException {
        //GIVEN
        when(currencyConverterFacade.getCountryForCurrencyCode(base)).thenReturn("Hungarian Forint");

        //WHEN
        ResponseEntity<String> responseEntity = currencyConverterController.getCountryForCurrencyCode(base);
        //THEN
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.hasBody());
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).contentEquals("Hungarian Forint"));
    }

    @Test
    void getCountryForCurrencyCodeWithInvalidCode() throws ResourceNotFoundException {
        //GIVEN
        when(currencyConverterFacade.getCountryForCurrencyCode("HHH"))
                .thenThrow(new ResourceNotFoundException("Not a valid currency code"));

        //WHEN
        //THEN
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> currencyConverterController.getCountryForCurrencyCode("HHH"));

        assertEquals("Not a valid currency code", exception.getMessage());
    }

    @Test
    void getCurrencyRate() {
        //GIVEN
        when(currencyConverterFacade.getCurrencyRate(base)).thenReturn(converter);

        //WHEN
        ResponseEntity<CurrencyConverter> responseEntity = currencyConverterController.getCurrencyRate(base);

        //THEN
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.hasBody());
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).getBase().contentEquals(base));
    }

    @Test
    void getHighestAndLowestCurrencyRates() {
        //GIVEN
        Map<String, Double> rates = new HashMap<>();
        rates.put("GBP", 0.0025654372);
        rates.put("IDR", 45.60031709);

        when(currencyConverterFacade.getHighestAndLowestCurrencyRate(base)).thenReturn(rates);

        //WHEN
        ResponseEntity<Map<String, Double>> responseEntity = currencyConverterController
                .getHighestAndLowestCurrencyRates(base);

        //THEN
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.hasBody());
        assertNotNull(Objects.requireNonNull(responseEntity.getBody()).get("IDR"));
    }
}