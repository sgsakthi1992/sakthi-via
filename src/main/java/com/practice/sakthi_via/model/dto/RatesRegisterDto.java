package com.practice.sakthi_via.model.dto;

import com.practice.sakthi_via.validator.TargetCode;

import javax.validation.constraints.Size;
import java.util.Set;

public class RatesRegisterDto {
    /**
     * Currency code length.
     */
    private static final int CURRENCY_CODE_LENGTH = 3;
    /**
     * Employee id.
     */
    private Long id;
    /**
     * Base currency code.
     */
    @Size(min = CURRENCY_CODE_LENGTH,
            max = CURRENCY_CODE_LENGTH,
            message = "Currency code must be of 3 letters")
    private String base;
    /**
     * Target currency codes.
     */
    @TargetCode
    private Set<String> target;

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
    public Set<String> getTarget() {
        return target;
    }

    /**
     * Setter for target currency codes.
     *
     * @param target target currency codes
     */
    public void setTarget(final Set<String> target) {
        this.target = target;
    }

    /**
     * Parameterized constructor.
     *
     * @param id     employee id
     * @param base   currency code
     * @param target currency codes
     */
    public RatesRegisterDto(final Long id, final String base,
                            final Set<String> target) {
        this.id = id;
        this.base = base;
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
