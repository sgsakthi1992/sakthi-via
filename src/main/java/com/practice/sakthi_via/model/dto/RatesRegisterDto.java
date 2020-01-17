package com.practice.sakthi_via.model.dto;

import java.util.List;

public class RatesRegisterDto {
    /**
     * Employee id.
     */
    private Long id;
    /**
     * Base currency code.
     */
    private String base;
    /**
     * Target currency codes.
     */
    private List<String> target;

    /**
     * Getter for employee id.
     *
     * @return employee id
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for employee id.
     *
     * @param id employee id
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Getter for base currency code.
     *
     * @return base currency code
     */
    public String getBase() {
        return base;
    }

    /**
     * Setter for base currency code.
     *
     * @param base base currency code
     */
    public void setBase(final String base) {
        this.base = base;
    }

    /**
     * Getter for target currency codes.
     *
     * @return target currency codes
     */
    public List<String> getTarget() {
        return target;
    }

    /**
     * Setter for target currency codes.
     *
     * @param target target currency codes
     */
    public void setTarget(final List<String> target) {
        this.target = target;
    }

    /**
     * Overridden toString method.
     *
     * @return String
     */
    @Override
    public String toString() {
        return "RatesRegister{"
                + "id=" + id
                + ", base='" + base + '\''
                + ", target=" + target
                + '}';
    }
}
