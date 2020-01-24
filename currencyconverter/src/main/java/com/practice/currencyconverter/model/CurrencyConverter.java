package com.practice.currencyconverter.model;

import java.time.LocalDate;
import java.util.Map;

public class CurrencyConverter {

    /**
     * to-do id.
     */
    private Map<String, Double> rates;
    /**
     * to-do title.
     */
    private String base;
    /**
     * to-do Status.
     */
    private LocalDate date;

    /**
     * Getter for rates.
     *
     * @return rates
     */
    public Map<String, Double> getRates() {
        return rates;
    }

    /**
     * Setter for rates.
     *
     * @param rates rates
     */
    public void setRates(final Map<String, Double> rates) {
        this.rates = rates;
    }

    /**
     * Getter for base currency.
     *
     * @return base currency
     */
    public String getBase() {
        return base;
    }

    /**
     * Setter for base currency.
     *
     * @param base base currency
     */
    public void setBase(final String base) {
        this.base = base;
    }

    /**
     * Getter for date.
     *
     * @return date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Setter for date.
     *
     * @param date date
     */
    public void setDate(final LocalDate date) {
        this.date = date;
    }

    /**
     * Overridden toString method.
     *
     * @return String
     */
    @Override
    public String toString() {
        return "CurrencyConverter{"
                + "rates=" + rates
                + ", base='" + base + '\''
                + ", date='" + date + '\''
                + '}';
    }
}
