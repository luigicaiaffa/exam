package org.exam.java.exam.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @PositiveOrZero(message = "Value must be positive")
    @NotNull(message = "Value cannot be null")
    private Integer value;

    @NotNull
    private Boolean hasHonors;

    @OneToOne
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

    public Grade(
            @PositiveOrZero(message = "Value must be positive") @NotNull(message = "Value cannot be null") Integer value,
            @NotNull Boolean hasHonors, Boolean isCancelled, Exam exam) {
        this.value = value;
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
