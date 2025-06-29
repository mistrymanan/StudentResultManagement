package com.example.studentresultmanagementsystem.repository;

import com.example.studentresultmanagementsystem.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


//it's an interface that extends JpaRepository to provide CRUD operations for Course entity.
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course save(Course course);
    Page<Course> findAllByDeletedFalse(Pageable pageable);
    boolean existsByIdAndDeletedFalse(Long id);
    Optional<Course> findByIdAndDeletedFalse(Long id);
    boolean existsByNameAndDeletedFalse(String name);

    @Override
    @Modifying
    @Query("UPDATE Course c SET c.deleted = true WHERE c.id = :id")
    void deleteById(@Param("id") Long id);
}
