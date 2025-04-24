package org.exam.java.exam.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "exams")
public class Exam {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Exam name cannot be blank")
    private String name;

    @Positive(message = "Number of CFU must be positive")
    @NotNull(message = "Number of CFU cannot be null")
    private Integer cfu;
    
    @Positive(message = "Course year must be a positive number")
    @NotNull(message = "Course year cannot be null")
    private Integer courseYear;
    
    @NotNull
    private Boolean isOptional;
    
    @NotNull
    private Boolean isPassed;

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

    public Integer getCfu() {
        return cfu;
    }

    public void setCfu(Integer cfu) {
        this.cfu = cfu;
    }

    public Integer getCourseYear() {
        return courseYear;
    }

    public void setCourseYear(Integer courseYear) {
        this.courseYear = courseYear;
    }

    public Boolean getIsOptional() {
        return isOptional;
    }

    public void setIsOptional(Boolean isOptional) {
        this.isOptional = isOptional;
    }

    public Boolean getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(Boolean isPassed) {
        this.isPassed = isPassed;
    }
    
    // # Constructors
    public Exam() {
    }
    
    public Exam(@NotBlank(message = "Exam name cannot be blank") String name,
    @Positive(message = "Number of CFU must be positive") @NotNull(message = "Number of CFU cannot be null") Integer cfu,
    @Positive(message = "Course year must be a positive number") @NotNull(message = "Course year cannot be null") Integer courseYear,
    @NotNull Boolean isOptional, @NotNull Boolean isPassed) {
        this.name = name;
        this.cfu = cfu;
        this.courseYear = courseYear;
        this.isOptional = isOptional;
        this.isPassed = isPassed;
    }

    @Override
    public String toString() {
        return "Exam [name=" + name + "]";
    }
    
}
