package org.exam.java.exam.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Positive(message = "Value must be positive")
    @NotNull(message = "Value cannot be null")
    private Integer value;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @NotNull
    private Boolean hasHonors;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    // # Getters / Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getHasHonors() {
        return hasHonors;
    }

    public void setHasHonors(Boolean hasHonors) {
        this.hasHonors = hasHonors;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    // # Constructors
    public Grade() {
    }

    public Grade(@Positive(message = "Value must be positive") @NotNull(message = "Value cannot be null") Integer value,
            @NotNull(message = "Date cannot be null") LocalDate date, @NotNull Boolean hasHonors, Exam exam) {
        this.value = value;
        this.date = date;
        this.hasHonors = hasHonors;
        this.exam = exam;
    }

    // # Methods
    @Override
    public String toString() {

        if (this.hasHonors) {
            return this.value + "L";
        }

        return this.value + "";
    }

}
