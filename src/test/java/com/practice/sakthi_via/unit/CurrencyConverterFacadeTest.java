/*
 * Copyright (c) 2020.
 */

package com.practice.sakthi_via.unit;

import com.practice.sakthi_via.facade.CurrencyConverterFacade;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class CurrencyConverterFacadeTest {

    CurrencyConverterFacade currencyConverterFacade = new CurrencyConverterFacade();

    CurrencyConverterFacade spyCurrencyConverterFacade = spy(currencyConverterFacade);

    @Test
    void getCountriesAndCurrencies() {
    }

    @Test
    void getCurrencyRate() {
    }

    @Test
    void getHighestAndLowestCurrencyRate() {
    }

    @Test
    void getCountryForCurrencyCode() {
        //GIVEN
        HashMap<String, String> countries = new HashMap<>();
        countries.put("HUF", "Hungarian Forint");
        countries.put("INR", "Indian Rupee");
        countries.put("USD", "US Dollar");
        when(spyCurrencyConverterFacade.getCountriesAndCurrencies()).thenReturn(countries);
        //WHEN
        String country = spyCurrencyConverterFacade.getCountryForCurrencyCode("HUF");
        //THEN
        assertEquals("Hungarian Forint", country);
    }
}