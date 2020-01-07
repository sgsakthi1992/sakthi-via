package com.practice.sakthi_via.model;


import com.practice.sakthi_via.constants.Constants;
import com.practice.sakthi_via.validator.Username;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Employee Model.
 *
 * @author Sakthi_Subramaniam
 */
@Entity
@ApiModel(value = "Employee Model", description = "Details about Employee")
public class Employee {
    /**
     * Employee Id.
     */
    @Id
    @SequenceGenerator(name = "employeeIdSeq",
            initialValue = Constants.EMPLOYEE_SEQUENCE_INITIAL)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "employeeIdSeq")
    @Column(nullable = false)
    @ApiModelProperty(notes = "Auto generated Employee Id")
    private Long id;

    /**
     * Employee name.
     */
    @NotEmpty(message = "Name is required")
    @Column(nullable = false)
    @ApiModelProperty(notes = "Employee name")
    private String name;

    /**
     * Employee username.
     */
    @Username
    @NotEmpty(message = "Username is required")
    @Size(min = Constants.EMPLOYEE_USERNAME_MIN_VALUE,
            max = Constants.EMPLOYEE_USERNAME_MAX_VALUE,
            message = Constants.EMPLOYEE_USERNAME_SIZE)
    @Column(nullable = false, updatable = false)
    @ApiModelProperty(notes = Constants.EMPLOYEE_USERNAME_SIZE)
    private String username;

    /**
     * Employee Email.
     */
    @NotEmpty(message = "Email id is required")
    @Email(message = Constants.EMAIL_VALIDATION_MSG)
    @Column(nullable = false)
    @ApiModelProperty(notes = "Employee email id")
    private String email;

    /**
     * Employee Age.
     */
    @Column(nullable = false)
    @Min(value = Constants.EMPLOYEE_AGE_MIN_VALUE,
            message = Constants.EMPLOYEE_MINIMUM_AGE)
    @ApiModelProperty(notes = "Employee age")
    private Integer age;

    /**
     * Parameterized constructor.
     *
     * @param id
     * @param name
     * @param username
     * @param email
     * @param age
     */
    public Employee(final Long id, final String name,
                    final String username, final String email,
                    final Integer age) {
        super();
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.age = age;
    }

    /**
     * Default constructor.
     */
    public Employee() {
        super();
    }

    /**
     * Getter for Employee Id.
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for Employee Id.
     *
     * @param id
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Getter for Employee Name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for Employee Name.
     *
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Getter for Employee Username.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for Employee Username.
     *
     * @param username
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Getter for Employee Email.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for Employee email.
     *
     * @param email
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Getter for Employee age.
     *
     * @return age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Setter for Employee age.
     *
     * @param age
     */
    public void setAge(final Integer age) {
        this.age = age;
    }

    /**
     * Overridden toString method.
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Employee{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", username='" + username + '\''
                + ", email='" + email + '\''
                + ", age=" + age
                + '}';
    }

}
