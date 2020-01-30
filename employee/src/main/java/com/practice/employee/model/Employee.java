package com.practice.employee.model;


import com.practice.employee.validator.Username;
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

@Entity
@ApiModel(value = "Employee Model", description = "Details about Employee")
public class Employee {
    /**
     * Message for failed Email validation.
     */
    private static final String EMAIL_VALIDATION_MSG
            = "Not a well-formed email address";
    /**
     * Message for Employee Username size.
     */
    private static final String EMPLOYEE_USERNAME_SIZE
            = "Username must have minimum 4 and maximum 10 characters";
    /**
     * Message for Employee minimum age.
     */
    private static final String EMPLOYEE_MINIMUM_AGE
            = "Employee must be 20 years old";
    /**
     * Employee sequence initial value.
     */
    private static final int EMPLOYEE_SEQUENCE_INITIAL = 40000;
    /**
     * Employee username min value.
     */
    private static final int EMPLOYEE_USERNAME_MIN_VALUE = 4;
    /**
     * Employee username max value.
     */
    private static final int EMPLOYEE_USERNAME_MAX_VALUE = 10;
    /**
     * Employee age minimum value.
     */
    private static final int EMPLOYEE_AGE_MIN_VALUE = 20;

    /**
     * Employee Id.
     */
    @Id
    @SequenceGenerator(name = "employeeIdSeq",
            initialValue = EMPLOYEE_SEQUENCE_INITIAL)
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
    @Size(min = EMPLOYEE_USERNAME_MIN_VALUE,
            max = EMPLOYEE_USERNAME_MAX_VALUE,
            message = EMPLOYEE_USERNAME_SIZE)
    @Column(nullable = false, updatable = false)
    @ApiModelProperty(notes = EMPLOYEE_USERNAME_SIZE)
    private String username;

    /**
     * Employee Email.
     */
    @NotEmpty(message = "Email id is required")
    @Email(message = EMAIL_VALIDATION_MSG)
    @Column(nullable = false)
    @ApiModelProperty(notes = "Employee email id")
    private String email;

    /**
     * Employee phone number.
     */
    @NotEmpty(message = "Phone Number is required")
    private String phoneNumber;

    /**
     * Employee Age.
     */
    @Column(nullable = false)
    @Min(value = EMPLOYEE_AGE_MIN_VALUE,
            message = EMPLOYEE_MINIMUM_AGE)
    @ApiModelProperty(notes = "Employee age")
    private Integer age;

    /**
     * Parameterized constructor.
     *
     * @param id          Employee id
     * @param name        Employee Name
     * @param username    Employee username
     * @param email       Employee email
     * @param phoneNumber Employee Phone Number
     * @param age         Employee age
     */
    public Employee(final Long id, final String name,
                    final String username, final String email,
                    final String phoneNumber, final Integer age) {
        super();
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
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
     * @param id Employee Id
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
     * @param name Employee name
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
     * @param username Employee username
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
     * @param email Employee email
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
     * @param age Employee age
     */
    public void setAge(final Integer age) {
        this.age = age;
    }

    /**
     * Getter for Employee phone number.
     *
     * @return phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Setter for Employee phone number.
     *
     * @param phoneNumber phone number
     */
    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
                + ", phoneNumber='" + phoneNumber + '\''
                + ", age=" + age
                + '}';
    }

}
