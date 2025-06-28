package com.example.studentresultmanagementsystem.entity;

import com.example.studentresultmanagementsystem.validation.MinimumAge;
import com.example.studentresultmanagementsystem.validation.ValidateEmailDomain;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

//Student is the entity class that maps to the "students" table in the database.
@Data
@ToString
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @Column(name = "family_name", nullable = false)
    @NotBlank(message = "Family Name is mandatory")
    private String familyName;

    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @ValidateEmailDomain
    private String email;

    @Column(name = "date_of_birth", nullable = false)
    @NotNull(message = "Date of Birth cannot be null.")
    @Past(message = "Date of Birth required to be the past date.")
    @MinimumAge(value = 10, message = "Student must be at least 10 years old")
    private LocalDate dateOfBirth;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public Student() {
    }

    public Student(String firstName, String familyName, String email, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.familyName = familyName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

}
