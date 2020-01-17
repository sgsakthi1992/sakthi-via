package com.practice.sakthi_via.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Set;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames =
                { "employee_id", "base" }) })
public class RatesRegister {
    /**
     * Registration id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer registrationId;
    /**
     * Employee id.
     */
    @OneToOne(cascade = CascadeType.ALL)
    private Employee employee;
    /**
     * Base currency code.
     */
    @Column
    private String base;
    /**
     * Target currency codes.
     */

    @ElementCollection
    @Column
    private Set<String> target;

    /**
     * Getter for employee id.
     *
     * @return employee id
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Setter for employee id.
     *
     * @param employee employee id
     */
    public void setEmployee(final Employee employee) {
        this.employee = employee;
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
     * Overridden toString method.
     *
     * @return String
     */
    @Override
    public String toString() {
        return "RatesRegister{"
                + "registrationId=" + registrationId
                + ", id=" + employee
                + ", base='" + base + '\''
                + ", target=" + target
                + '}';
    }
}
