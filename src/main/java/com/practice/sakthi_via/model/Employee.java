package com.practice.sakthi_via.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@ApiModel(value = "Employee Model", description = "Details about Employee")
public class Employee {
    @Id
    @SequenceGenerator(name = "employeeIdSeq", initialValue = 40000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeIdSeq")
    @Column(updatable = false)
    @ApiModelProperty(notes = "Auto generated Employee Id")
    private Long id;

    @NotEmpty(message = "Name is required")
    @Column
    @ApiModelProperty(notes = "Employee name")
    private String name;

    @NotEmpty(message = "Username is required")
    @Column(updatable = false)
    @ApiModelProperty(notes = "Employee username")
    private String username;

    @NotEmpty(message = "Email id is required")
    @Email(message = "Not a valid email address")
    @Column
    @ApiModelProperty(notes = "Employee email id")
    private String email;

    @NotNull(message = "Age is required")
    @Column
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

    @Email(message = "Not a valid email address")
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
