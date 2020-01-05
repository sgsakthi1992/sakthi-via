package com.practice.sakthi_via.model;


import com.practice.sakthi_via.validator.Username;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@ApiModel(value = "Employee Model", description = "Details about Employee")
public class Employee {
    @Id
    @SequenceGenerator(name = "employeeIdSeq", initialValue = 40000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeIdSeq")
    @Column(nullable = false)
    @ApiModelProperty(notes = "Auto generated Employee Id")
    private Long id;

    @NotEmpty(message = "Name is required")
    @Column(nullable = false)
    @ApiModelProperty(notes = "Employee name")
    private String name;

    @Username
    @NotEmpty(message = "Username is required")
    @Size(min = 4, max = 10, message = "Username must have minimum 4 and maximum 10 characters")
    @Column(nullable = false, updatable = false)
    @ApiModelProperty(notes = "Employee unique username minimum 4 and maximum 10 characters")
    private String username;

    @NotEmpty(message = "Email id is required")
    @Email(message = "Not a well-formed email address")
    @Column(nullable = false)
    @ApiModelProperty(notes = "Employee email id")
    private String email;

    @NotNull(message = "Age is required")
    @Column(nullable = false)
    @Min(value = 20, message = "Employee must be 20 years old")
    @ApiModelProperty(notes = "Employee age")
    private Integer age;

    public Employee() {
        super();
    }

    public Employee(Long id, String name, String username, String email, Integer age) {
        super();
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }

}
