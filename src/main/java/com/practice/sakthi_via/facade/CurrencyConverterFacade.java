package com.practice.sakthi_via.facade;

import com.practice.sakthi_via.model.CurrencyConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class CurrencyConverterFacade {
    /**
     * RestTemplate object.
     */
    private RestTemplate restTemplate = new RestTemplate();

    /**
     * Get Countries and their currencies from
     * https://openexchangerates.org/api/currencies.json.
     *
     * @return Countries and their currencies
     */
    public HashMap<String, String> getCountriesAndCurrencies() {
        String url = "https://openexchangerates.org/api/currencies.json";
        return restTemplate.getForObject(url, HashMap.class);
    }

    /**
     * Get Currency conversion rate from https://api.exchangeratesapi.io/latest.
     *
     * @param base base currency
     * @return currency rates for the base currency
     */
    public CurrencyConverter getCurrencyRate(final String base) {
        String url = String.format(
                "https://api.exchangeratesapi.io/latest?base=%s", base);
        CurrencyConverter response = restTemplate
                .getForObject(url, CurrencyConverter.class);
        return response;
    }

    /**
     * Get Highest currency rate country for the base currency.
     *
     * @param base base currency
     * @return highest currency rate country
     */
    public Set<Map.Entry> getHighestAndLowestCurrencyRate(
            final String base) {
        CurrencyConverter currencyRate = getCurrencyRate(base);
        HashMap<String, Double> rates = currencyRate.getRates();

        Optional<Map.Entry<String, Double>> max = rates.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue));

        Optional<Map.Entry<String, Double>> min = rates.entrySet().stream()
                .min(Comparator.comparing(Map.Entry::getValue));
        Set<Map.Entry> highAndLowRates = new HashSet<>();
        max.ifPresent(highAndLowRates::add);
        min.ifPresent(highAndLowRates::add);
        return highAndLowRates;
    }

    /**
     * Get Country for Currency code.
     *
     * @param code currency
     * @return country
     */
    public String getCountryForCurrencyCode(final String code) {
        HashMap<String, String> countries = getCountriesAndCurrencies();
        return countries.get(code);
    }
}
