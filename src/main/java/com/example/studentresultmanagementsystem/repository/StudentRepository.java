package com.example.studentresultmanagementsystem.repository;

import com.example.studentresultmanagementsystem.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


//it's an interface that extends JpaRepository to provide CRUD operations for Student entity.
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student save(Student student);
    Optional<Student> findByIdAndDeletedFalse(Long id);
    Page<Student> findAllByDeletedFalse(Pageable pageable);
    boolean existsByEmailAndDeletedFalse(String email);
    boolean existsByIdAndDeletedFalse(Long id);

    @Override
    @Modifying
    @Query("UPDATE Student s SET s.deleted = true WHERE s.id = :id")
    void deleteById(@Param("id") Long id);
}
