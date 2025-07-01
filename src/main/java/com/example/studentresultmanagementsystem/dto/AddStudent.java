package com.example.studentresultmanagementsystem.dto;

import com.example.studentresultmanagementsystem.validation.MinimumAge;
import com.example.studentresultmanagementsystem.validation.ValidateEmailDomain;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

//AddStudent is a Data Transfer Object (DTO) for adding a new student.
public class AddStudent {
    @NotBlank(message = "First name is mandatory")
    @Size(min = 2, max = 50, message = "First Name must be between 2 to 50 characters")
    private String firstName;

    @NotBlank(message = "Family Name is mandatory")
    @Size(min = 2, max = 50, message = "Family Name must between 2 to 50 characters")
    private String familyName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @ValidateEmailDomain
    private String email;

    @NotNull(message = "Date of Birth cannot be null.")
    @Past(message = "Date of Birth required to be the past date.")
    @MinimumAge(value = 10, message = "Student must be at least 10 years old")
    private LocalDate dateOfBirth;

    @Override
    public String toString() {
        return "AddStudent{" +
                "firstName='" + firstName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }

    public AddStudent() {
    }

    public AddStudent(String firstName) {
        this.firstName = firstName;
    }

    public AddStudent(String firstName, String familyName, String email, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.familyName = familyName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public AddStudent setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getFamilyName() {
        return familyName;
    }

    public AddStudent setFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AddStudent setEmail(String email) {
        this.email = email;
        return this;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public AddStudent setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }
}
