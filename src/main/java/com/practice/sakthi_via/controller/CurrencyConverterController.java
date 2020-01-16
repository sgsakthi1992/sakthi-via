package com.practice.sakthi_via.controller;

import com.practice.sakthi_via.exception.ResourceNotFoundException;
import com.practice.sakthi_via.facade.CurrencyConverterFacade;
import com.practice.sakthi_via.model.CurrencyConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Validated
public class CurrencyConverterController {
    /**
     * Currency code length.
     */
    private static final int CURRENCY_CODE_LENGTH = 3;
    /**
     * TodoFacade object.
     */
    private CurrencyConverterFacade currencyConverterFacade;

    /**
     * Parameterized constructor to bind TodoFacade object.
     *
     * @param currencyConverterFacade TodoFacade object
     */
    public CurrencyConverterController(
            final CurrencyConverterFacade currencyConverterFacade) {
        this.currencyConverterFacade = currencyConverterFacade;
    }

    /**
     * API to get Countries and their currencies from external source.
     *
     * @return ResponseEntity with Country list
     */
    @GetMapping("/countries")
    public ResponseEntity<Map> getCountriesAndCurrencies() {
        Map countryList = currencyConverterFacade
                .getCountriesAndCurrencies();
        return ResponseEntity.status(HttpStatus.OK).body(countryList);
    }

    /**
     * API to get Countries Name for the currency code.
     *
     * @param code currency code
     * @return ResponseEntity with Users List
     * @throws ResourceNotFoundException currency code not found
     */
    @GetMapping("/country/{code}")
    public ResponseEntity<String> getCountryForCurrencyCode(
            @Size(min = CURRENCY_CODE_LENGTH, max = CURRENCY_CODE_LENGTH,
                    message = "Currency code must be of 3 letters")
            @PathVariable(value = "code") final String code)
            throws ResourceNotFoundException {
        String country = currencyConverterFacade
                .getCountryForCurrencyCode(code);
        return ResponseEntity.status(HttpStatus.OK).body(country);
    }

    /**
     * API to get currency rate for base country.
     *
     * @param base base country
     * @return list of to-do's
     */
    @GetMapping("/rates")
    public ResponseEntity<CurrencyConverter> getCurrencyRate(
            @Size(min = CURRENCY_CODE_LENGTH, max = CURRENCY_CODE_LENGTH,
                    message = "Currency code must be of 3 letters")
            @RequestParam(value = "base") final String base) {
        CurrencyConverter currencyRate = currencyConverterFacade
                .getCurrencyRate(base);
        return ResponseEntity.status(HttpStatus.OK).body(currencyRate);
    }

    /**
     * API to get highest currency rate country for base country.
     *
     * @param base base country
     * @return highest currency rate country
     */
    @GetMapping("/highestCurrencyRate")
    public ResponseEntity<Map<String, Double>> getHighestCurrencyRate(
            @RequestParam(value = "base") final String base) {
        Map<String, Double> currencyRate = currencyConverterFacade
                .getHighestAndLowestCurrencyRate(base);
        return ResponseEntity.status(HttpStatus.OK).body(currencyRate);
    }
}
