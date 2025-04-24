package org.exam.java.exam.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Surname cannot be blank")
    private String surname;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Degree Course cannot be blank")
    private String degreeCourse;

    @Positive(message = "Number of total CFU must be positive")
    @NotNull(message = "Number of total CFU cannot be null")
    private Integer totalCfu;

    // # Getters / Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDegreeCourse() {
        return degreeCourse;
    }

    public void setDegreeCourse(String degreeCourse) {
        this.degreeCourse = degreeCourse;
    }

    public Integer getTotalCfu() {
        return totalCfu;
    }

    public void setTotalCfu(Integer totalCfu) {
        this.totalCfu = totalCfu;
    }

    // # Constructors
    public User() {
    }

    public User(@NotBlank(message = "Name cannot be blank") String name,
            @NotBlank(message = "Surname cannot be blank") String surname,
            @NotBlank(message = "Username cannot be blank") String username,
            @Email(message = "Email should be valid") @NotBlank(message = "Email cannot be blank") String email,
            @Size(min = 8, message = "Password must be at least 8 characters long") @NotBlank(message = "Password cannot be blank") String password,
            @NotBlank(message = "Degree Course cannot be blank") String degreeCourse,
            @NotBlank(message = "Number of total CFU cannot be blank") Integer totalCfu) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.degreeCourse = degreeCourse;
        this.totalCfu = totalCfu;
    }

    // # Methods
    @Override
    public String toString() {
        return this.username;
    }

}
