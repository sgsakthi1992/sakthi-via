package com.practice.currencyconverter.unit;

import com.practice.currencyconverter.facade.CurrencyConverterFacade;
import com.practice.currencyconverter.model.CurrencyConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "via.countries.api.url = https://openexchangerates.org/api/currencies.json",
        "via.currencyrate.api.url = https://api.exchangeratesapi.io/latest?base=%s",
        "via.rateswithtargets.api.url = https://api.exchangeratesapi.io/latest?symbols=%s&base=%s"
})
public class CurrencyConverterFallbackUnitTest {
    @Configuration
    @EnableAutoConfiguration
    @EnableHystrix
    public static class SpringConfig {
        @Bean
        RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        public CurrencyConverterFacade currencyConverterFacade() {
            return new CurrencyConverterFacade(restTemplate());
        }
    }

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private CurrencyConverterFacade currencyConverterFacade;

    @Test
    void getCountriesAndCurrenciesFallBackMethod() {
        //GIVEN
        when(restTemplate.getForObject("https://openexchangerates.org/api/currencies.json", HashMap.class))
                .thenThrow(RuntimeException.class);

        //WHEN
        Map countriesAndCurrencies = currencyConverterFacade.getCountriesAndCurrencies();

        //THEN
        assertNotNull(countriesAndCurrencies);
        assertNotNull(countriesAndCurrencies.get("INR"));
        assertNotNull(countriesAndCurrencies.get("HUF"));
    }

    @Test
    void getCurrencyRateFallBackMethod() {
        //GIVEN
        when(restTemplate.getForObject("https://api.exchangeratesapi.io/latest?base=HUF", CurrencyConverter.class))
                .thenThrow(RuntimeException.class);

        //WHEN
        CurrencyConverter currencyRate = currencyConverterFacade.getCurrencyRate("HUF");

        //THEN
        assertNotNull(currencyRate);
        assertEquals("HUF", currencyRate.getBase());
    }
}
