package com.example.studentresultmanagementsystem.repository;

import com.example.studentresultmanagementsystem.entity.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//it's an interface that extends JpaRepository to provide CRUD operations for Results entity.
@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    Result save(Result result);
    Page<Result> findAllByDeletedFalse(Pageable pageable);
    Optional<Result> findByIdAndDeletedFalse(Long id);
    //    @EntityGraph(attributePaths = {"course", "student"})
    Page<Result> findAllByCourseIdAndDeletedFalse(Long courseId, Pageable pageable);
    boolean existsByIdAndDeletedFalse(Long id);
    boolean existsByCourseIdAndStudentIdAndDeletedFalse(Long courseId, Long studentId);

    @Modifying
    @Query("UPDATE Result r set r.deleted = true WHERE r.course.id = :courseId")
    void deleteResultsByCourseId(Long courseId);

    @Modifying
    @Query("UPDATE Result r set r.deleted = true WHERE r.student.id = :studentId")
    void deleteResultsByStudentId(Long studentId);

    @Override
    @Modifying
    @Query("UPDATE Result r SET r.deleted = true WHERE r.id = :id")
    void deleteById(@Param("id") Long id);
}
