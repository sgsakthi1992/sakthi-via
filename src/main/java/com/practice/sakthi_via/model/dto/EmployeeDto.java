package com.practice.sakthi_via.model.dto;

import com.practice.sakthi_via.constants.Constants;
import com.practice.sakthi_via.validator.Username;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class EmployeeDto {

    /**
     * Employee name.
     */
    @NotEmpty(message = "Name is required")
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
    @ApiModelProperty(notes = Constants.EMPLOYEE_USERNAME_SIZE)
    private String username;

    /**
     * Employee Email.
     */
    @NotEmpty(message = "Email id is required")
    @Email(message = Constants.EMAIL_VALIDATION_MSG)
    @ApiModelProperty(notes = "Employee email id")
    private String email;

    /**
     * Employee Age.
     */
    @Min(value = Constants.EMPLOYEE_AGE_MIN_VALUE,
            message = Constants.EMPLOYEE_MINIMUM_AGE)
    @ApiModelProperty(notes = "Employee age")
    private Integer age;

    /**
     * Parameterized constructor.
     *
     * @param name Employee Name
     * @param username Employee Username
     * @param email Employee email
     * @param age Employee age
     */
    public EmployeeDto(final String name,
                       final String username, final String email,
                       final Integer age) {
        super();
        this.name = name;
        this.username = username;
        this.email = email;
        this.age = age;
    }

    /**
     * Default constructor.
     */
    public EmployeeDto() {
        super();
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
                + ", name='" + name + '\''
                + ", username='" + username + '\''
                + ", email='" + email + '\''
                + ", age=" + age
                + '}';
    }
}
