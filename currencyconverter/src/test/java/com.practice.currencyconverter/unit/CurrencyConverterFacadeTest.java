package com.practice.currencyconverter.unit;

import com.practice.exception.ResourceNotFoundException;
import com.practice.currencyconverter.facade.CurrencyConverterFacade;
import com.practice.currencyconverter.model.CurrencyConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CurrencyConverterFacadeTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    CurrencyConverterFacade currencyConverterFacade;

    @Spy
    @InjectMocks
    CurrencyConverterFacade spyCurrencyConverterFacade;

    private static final String COUNTRIES_AND_CURRENCIES_URL =
            "https://openexchangerates.org/api/currencies.json";
    private static final String CURRENCY_RATE_URL =
            "https://api.exchangeratesapi.io/latest?";

    private String base = "HUF";
    private CurrencyConverter converter;
    private HashMap<String, String> countries;

    CurrencyConverterFacadeTest() {
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
        ReflectionTestUtils.setField(currencyConverterFacade, "countriesAndCurrenciesUrl",
                "https://openexchangerates.org/api/currencies.json");
        when(restTemplate.getForObject(COUNTRIES_AND_CURRENCIES_URL, HashMap.class)).thenReturn(countries);

        //WHEN
        Map countriesAndCurrencies = currencyConverterFacade.getCountriesAndCurrencies();

        //THEN
        assertEquals(countries.size(), countriesAndCurrencies.size());
        assertEquals(countries.get("HUF"), countriesAndCurrencies.get("HUF"));
    }

    @Test
    void getCurrencyRate() {
        //GIVEN
        ArgumentCaptor captor = ArgumentCaptor.forClass(String.class);

        ReflectionTestUtils.setField(currencyConverterFacade, "currencyRateUrl",
                "https://api.exchangeratesapi.io/latest?base=%s");
        when(restTemplate.getForObject(CURRENCY_RATE_URL + "base=" + base, CurrencyConverter.class))
                .thenReturn(converter);

        //WHEN
        CurrencyConverter currencyRate = currencyConverterFacade.getCurrencyRate(base);

        //THEN
        verify(restTemplate).getForObject((String) captor.capture(), eq(CurrencyConverter.class));
        assertEquals(CURRENCY_RATE_URL + "base=" + base, captor.getValue());
        assertNotNull(currencyRate.getRates());
        assertNull(currencyRate.getRates().get("HUF"));
    }

    @Test
    void getHighestAndLowestCurrencyRate() {
        //GIVEN
        ReflectionTestUtils.setField(currencyConverterFacade, "currencyRateUrl",
                "https://api.exchangeratesapi.io/latest?base=%s");
        when(restTemplate.getForObject(CURRENCY_RATE_URL + "base=" + base, CurrencyConverter.class))
                .thenReturn(converter);

        //WHEN
        Map<String, Double> highestAndLowestCurrencyRate = currencyConverterFacade
                .getHighestAndLowestCurrencyRate(base);
        //THEN
        assertNotNull(highestAndLowestCurrencyRate.get("GBP"));
        assertNotNull(highestAndLowestCurrencyRate.get("IDR"));
        assertNull(highestAndLowestCurrencyRate.get("HUF"));
    }

    @Test
    void getCountryForCurrencyCode() throws ResourceNotFoundException {
        //GIVEN
        when(spyCurrencyConverterFacade.getCountriesAndCurrencies()).thenReturn(countries);

        //WHEN
        String country = spyCurrencyConverterFacade.getCountryForCurrencyCode(base);

        //THEN
        assertEquals("Hungarian Forint", country);
    }

    @Test
    void getCountryForCurrencyCodeWithInvalidCode() {
        //GIVEN
        when(spyCurrencyConverterFacade.getCountriesAndCurrencies()).thenReturn(countries);

        //WHEN
        //THEN
        assertThrows(ResourceNotFoundException.class,
                () -> spyCurrencyConverterFacade.getCountryForCurrencyCode("HHH"));

    }

    @Test
    void getCurrencyRateWithTarget() {
        //GIVEN
        ArgumentCaptor captor = ArgumentCaptor.forClass(String.class);
        ReflectionTestUtils.setField(currencyConverterFacade, "currencyRateWithTargetsUrl",
                "https://api.exchangeratesapi.io/latest?symbols=%s&base=%s");
        SortedSet<String> sortedSet = new TreeSet<>(Set.of("INR", "IDR", "GBP"));
        when(restTemplate.getForObject(CURRENCY_RATE_URL + "symbols=GBP,IDR,INR&base=" + base, CurrencyConverter.class))
                .thenReturn(converter);

        //WHEN
        CurrencyConverter currencyRateWithTarget = currencyConverterFacade
                .getCurrencyRateWithTarget(base, sortedSet);

        //THEN
        verify(restTemplate).getForObject((String) captor.capture(), eq(CurrencyConverter.class));
        assertTrue(captor.getValue().toString().contains("GBP,IDR,INR"));
        assertNotNull(currencyRateWithTarget.getRates());
        assertNull(currencyRateWithTarget.getRates().get("HUF"));
    }
}