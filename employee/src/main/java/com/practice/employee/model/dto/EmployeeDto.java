package com.practice.employee.model.dto;

import com.practice.employee.validator.Username;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class EmployeeDto {

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
  @Size(min = EMPLOYEE_USERNAME_MIN_VALUE,
      max = EMPLOYEE_USERNAME_MAX_VALUE,
      message = EMPLOYEE_USERNAME_SIZE)
  @ApiModelProperty(notes = EMPLOYEE_USERNAME_SIZE)
  private String username;

  /**
   * Employee Email.
   */
  @NotEmpty(message = "Email id is required")
  @Email(message = EMAIL_VALIDATION_MSG)
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
  @Min(value = EMPLOYEE_AGE_MIN_VALUE,
      message = EMPLOYEE_MINIMUM_AGE)
  @ApiModelProperty(notes = "Employee age")
  private Integer age;

  /**
   * Parameterized constructor.
   *
   * @param name        Employee Name
   * @param username    Employee Username
   * @param email       Employee email
   * @param phoneNumber Employee Phone Number
   * @param age         Employee age
   */
  public EmployeeDto(final String name,
      final String username, final String email,
      final String phoneNumber, final Integer age) {
    super();
    this.name = name;
    this.username = username;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.age = age;
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
   * @param name Employee Name
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
   * @param username Employee Username
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
   * @param email Employee Email
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
        + ", name='" + name + '\''
        + ", username='" + username + '\''
        + ", email='" + email + '\''
        + ", phoneNumber='" + phoneNumber + '\''
        + ", age=" + age
        + '}';
  }
}
