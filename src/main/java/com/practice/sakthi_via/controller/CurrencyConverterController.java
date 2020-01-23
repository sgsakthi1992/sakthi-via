package com.practice.sakthi_via.controller;

import com.practice.sakthi_via.exception.ResourceNotFoundException;
import com.practice.sakthi_via.facade.CurrencyConverterFacade;
import com.practice.sakthi_via.model.CurrencyConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Validated
@RequestMapping("/api/v1")
@Api("Currency Converter System")
public class CurrencyConverterController {
    /**
     * Currency code length.
     */
    private static final int CURRENCY_CODE_LENGTH = 3;
    /**
     * HTTP Status OK value.
     */
    private static final int HTTP_STATUS_OK = 200;
    /**
     * HTTP Status Bad Request value.
     */
    private static final int HTTP_STATUS_BAD_REQUEST = 400;
    /**
     * HTTP Status Not Found value.
     */
    private static final int HTTP_STATUS_NOT_FOUND = 404;
    /**
     * TodoFacade object.
     */
    private final CurrencyConverterFacade currencyConverterFacade;

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
    @ApiOperation("Retrieve Countries and their Currencies")
    @ApiResponses({
            @ApiResponse(code = HTTP_STATUS_OK,
                    message = "Retrieved Successfully")
    })
    @GetMapping("/countries")
    public ResponseEntity<Map> getCountriesAndCurrencies() {
        return ResponseEntity.status(HttpStatus.OK).body(currencyConverterFacade
                .getCountriesAndCurrencies());
    }

    /**
     * API to get Countries Name for the currency code.
     *
     * @param code currency code
     * @return ResponseEntity with Users List
     * @throws ResourceNotFoundException currency code not found
     */
    @ApiOperation("Get Country for currency code")
    @ApiResponses({
            @ApiResponse(code = HTTP_STATUS_OK,
                    message = "Retrieved Successfully"),
            @ApiResponse(code = HTTP_STATUS_BAD_REQUEST,
                    message = "Currency code must be of 3 letters"),
            @ApiResponse(code = HTTP_STATUS_NOT_FOUND,
                    message = "Not a Valid currency code")
    })
    @GetMapping("/country/{code}")
    public ResponseEntity<String> getCountryForCurrencyCode(
            @ApiParam(value = "Currency code", required = true)
            @Size(min = CURRENCY_CODE_LENGTH, max = CURRENCY_CODE_LENGTH,
                    message = "Currency code must be of 3 letters")
            @PathVariable(value = "code") final String code)
            throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(currencyConverterFacade.getCountryForCurrencyCode(code));
    }

    /**
     * API to get currency rate for base country.
     *
     * @param base base country
     * @return list of to-do's
     */
    @ApiOperation("Get Country for currency code")
    @ApiResponses({
            @ApiResponse(code = HTTP_STATUS_OK,
                    message = "Retrieved Successfully"),
            @ApiResponse(code = HTTP_STATUS_BAD_REQUEST,
                    message = "Currency code must be of 3 letters")
    })
    @GetMapping("/rates")
    public ResponseEntity<CurrencyConverter> getCurrencyRate(
            @ApiParam(value = "Currency code", required = true)
            @Size(min = CURRENCY_CODE_LENGTH, max = CURRENCY_CODE_LENGTH,
                    message = "Currency code must be of 3 letters")
            @RequestParam(value = "base") final String base) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(currencyConverterFacade.getCurrencyRate(base));
    }

    /**
     * API to get highest currency rate country for base country.
     *
     * @param base base country
     * @return highest currency rate country
     */
    @ApiOperation("Get Country for currency code")
    @ApiResponses({
            @ApiResponse(code = HTTP_STATUS_OK,
                    message = "Retrieved Successfully"),
            @ApiResponse(code = HTTP_STATUS_BAD_REQUEST,
                    message = "Currency code must be of 3 letters")
    })
    @GetMapping("/highestAndLowestCurrencyRates")
    public ResponseEntity<Map<String, Double>> getHighestAndLowestCurrencyRates(
            @ApiParam(value = "Currency code", required = true)
            @Size(min = CURRENCY_CODE_LENGTH, max = CURRENCY_CODE_LENGTH,
                    message = "Currency code must be of 3 letters")
            @RequestParam(value = "base") final String base) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(currencyConverterFacade
                        .getHighestAndLowestCurrencyRate(base));
    }
}
