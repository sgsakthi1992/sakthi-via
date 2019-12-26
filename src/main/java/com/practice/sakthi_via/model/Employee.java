package com.practice.sakthi_via.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@ApiModel(description = "Details about Employee")
public class Employee {
    @Id
    @SequenceGenerator(name = "employeeIdSeq", initialValue = 40000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeIdSeq")
    @ApiModelProperty(notes = "Auto generated Employee Id")
    private long id;
    @ApiModelProperty(notes = "Employee name")
    private String name;
    @ApiModelProperty(notes = "Employee username")
    private String username;
    @ApiModelProperty(notes = "Employee email id")
    private String email;
    @ApiModelProperty(notes = "Employee Age")
    private int age;

    public Employee() {
        super();
    }

    public Employee(long id, String name, String username, String email, int age) {
        super();
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
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
