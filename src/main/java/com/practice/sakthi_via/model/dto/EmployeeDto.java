package com.practice.sakthi_via.model.dto;

import com.practice.sakthi_via.constants.Constants;
import com.practice.sakthi_via.validator.Username;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class EmployeeDto {

    @NotEmpty(message = "Name is required")
    @ApiModelProperty(notes = "Employee name")
    private String name;

    @Username
    @NotEmpty(message = "Username is required")
    @Size(min = 4, max = 10, message = "Username must have minimum 4 and maximum 10 characters")
    @ApiModelProperty(notes = "Employee unique username minimum 4 and maximum 10 characters")
    private String username;

    @NotEmpty(message = "Email id is required")
    @Email(message = Constants.EMAIL_VALIDATION_MSG)
    @ApiModelProperty(notes = "Employee email id")
    private String email;

    @Min(value = 20, message = "Employee must be 20 years old")
    @ApiModelProperty(notes = "Employee age")
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "EmployeeDto{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
