package com.example.studentresultmanagementsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

//Course is the entity class that maps to the "courses" table in the database.
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "deleted"}, name = "unique_course_name_deleted")
})
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Course name is mandatory")
    private String name;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public Course() {
    }

    public Course(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Course setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Course setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Course setDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
}
