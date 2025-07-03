package com.example.studentresultmanagementsystem.controller;

import com.example.studentresultmanagementsystem.entity.Course;
import com.example.studentresultmanagementsystem.entity.Result;
import com.example.studentresultmanagementsystem.entity.Score;
import com.example.studentresultmanagementsystem.entity.Student;
import com.example.studentresultmanagementsystem.repository.CourseRepository;
import com.example.studentresultmanagementsystem.repository.ResultRepository;
import com.example.studentresultmanagementsystem.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureGraphQlTester
public class ResultControllerIntegrationTest {
    @Autowired
    private GraphQlTester graphQlTester;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {
        resultRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();

        student =
                studentRepository.save(
                        new Student("Manan", "Mistry", "mananmistry10@gmail.com", LocalDate.of(1999, 12, 28)));
        course = courseRepository.save(new Course("Java"));
    }

    @Test
    void shouldAddResultSuccessfully() {
        String mutation =
                """
                            mutation {
                                addResult(input: {
                                    studentId: %d,
                                    courseId: %d,
                                    score: A
                                }) {
                                    id
                                    student {
                                        id
                                        firstName
                                    }
                                    course {
                                        id
                                        name
                                    }
                                    score
                                }
                            }
                        """.formatted(student.getId(), course.getId());

        graphQlTester
                .document(mutation)
                .execute()
                .path("addResult.student.firstName")
                .entity(String.class)
                .isEqualTo("Manan")
                .path("addResult.course.name")
                .entity(String.class)
                .isEqualTo("Java")
                .path("addResult.score")
                .entity(String.class)
                .isEqualTo("A");

        Page<Result> results = resultRepository.findAllByDeletedFalse(Pageable.unpaged());
        assertThat(results.getContent()).hasSize(1);
        assertThat(results.getContent().get(0).getScore()).isEqualTo(Score.A);
    }

    @Test
    void shouldNotAddResultForNonExistentStudent() {
        String mutation =
                """
                        mutation {
                            addResult(input: {
                                studentId: 11,
                                courseId: %d,
                                score: B
                            }) {
                                id
                            }
                        }
                    """.formatted(course.getId());

        graphQlTester
                .document(mutation)
                .execute()
                .errors()
                .satisfy(
                        errors -> {
                            assertThat(errors).hasSize(1);
                            assertThat(errors.get(0).getMessage()).contains("Student Not Found with id: 11");
                            assertThat(errors.get(0).getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
                        });

        Page<Result> results = resultRepository.findAllByDeletedFalse(Pageable.unpaged());
        assertThat(results.getContent()).isEmpty();
    }

    @Test
    void shouldNotAddResultForNonExistentCourse() {
        String mutation =
                """
                        mutation {
                            addResult(input: {
                                studentId: %d,
                                courseId: 11,
                                score: C
                            }) {
                                id
                            }
                        }
                    """.formatted(student.getId());

        graphQlTester
                .document(mutation)
                .execute()
                .errors()
                .satisfy(
                        errors -> {
                            assertThat(errors).hasSize(1);
                            assertThat(errors.get(0).getMessage()).contains("Course Not Found with id: 11");
                            assertThat(errors.get(0).getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
                        });

        Page<Result> results = resultRepository.findAllByDeletedFalse(Pageable.unpaged());
        assertThat(results.getContent()).isEmpty();
    }

    @Test
    void shouldNotAddDuplicateResult() {
        resultRepository.save(new Result(student, course, Score.A));

        String mutation =
                """
                        mutation {
                                addResult(input: {
                                    studentId: %d,
                                    courseId: %d,
                                    score: A
                                }) {
                                    id
                                }
                            }
                        """.formatted(student.getId(), course.getId());

        graphQlTester
                .document(mutation)
                .execute()
                .errors()
                .satisfy(
                        errors -> {
                            assertThat(errors).hasSize(1);
                            assertThat(errors.get(0).getMessage()).contains("Result Already Exists!");
                        });

        Page<Result> results = resultRepository.findAllByDeletedFalse(Pageable.unpaged());
        assertThat(results.getContent()).hasSize(1);
    }

    @Test
    void shouldNotAddResultWithMissingFields() {
        String mutation =
                """
                       mutation {
                                addResult(input: {
                                    studentId: %d
                                }) {
                                    id
                                }
                            }
                        """.formatted(student.getId());

        graphQlTester
                .document(mutation)
                .execute()
                .errors()
                .satisfy(
                        errors -> {
                            assertThat(errors).hasSize(1);
                            assertThat(errors.get(0).getMessage())
                                    .contains("missing required fields '[courseId, score]'");
                        });

        Page<Result> results = resultRepository.findAllByDeletedFalse(Pageable.unpaged());
        assertThat(results.getContent()).isEmpty();
    }

    @Test
    void shouldNotAllowDuplicateResultForSameStudentAndCourse() {
        resultRepository.save(new Result(student, course, Score.A));

        String mutation =
                """
                        mutation {
                                addResult(input: {
                                    studentId: %d,
                                    courseId: %d,
                                    score: A
                                }) {
                                    id
                                }
                            }
                        """.formatted(student.getId(), course.getId());

        graphQlTester
                .document(mutation)
                .execute()
                .errors()
                .satisfy(
                        errors -> {
                            assertThat(errors).hasSize(1);
                            assertThat(errors.get(0).getMessage()).contains("Result Already Exists!");
                        });

        Page<Result> results = resultRepository.findAllByDeletedFalse(Pageable.unpaged());
        assertThat(results.getContent()).hasSize(1);
    }

    @Test
    void shouldDeleteResultsWhenStudentIsDeleted() {
        Result result = resultRepository.save(new Result(student, course, Score.B));

        Page<Result> initialResults = resultRepository.findAllByDeletedFalse(PageRequest.of(0, 10));
        assertThat(initialResults.getContent()).hasSize(1);

        String deleteStudentMutation =
                """
                        mutation {
                                deleteStudent(id: %d)
                            }
                        """.formatted(student.getId());

        graphQlTester
                .document(deleteStudentMutation)
                .execute()
                .path("deleteStudent")
                .entity(Boolean.class)
                .isEqualTo(true);

        Optional<Result> deletedResult = resultRepository.findById(result.getId());
        assertThat(deletedResult).isPresent();
        assertThat(deletedResult.get().isDeleted()).isTrue();

        Page<Result> activeResults = resultRepository.findAllByDeletedFalse(Pageable.unpaged());
        assertThat(activeResults.getContent()).isEmpty();
    }

    @Test
    void shouldSoftDeleteResultSuccessfully() {
        Result result = resultRepository.save(new Result(student, course, Score.B));

        String mutation =
                """
                        mutation {
                                deleteResult(id: %d)
                            }
                        """.formatted(result.getId());

        graphQlTester
                .document(mutation)
                .execute()
                .path("deleteResult")
                .entity(Boolean.class)
                .isEqualTo(true);

        Optional<Result> deletedResult = resultRepository.findByIdAndDeletedFalse(result.getId());
        assertThat(deletedResult).isEmpty();

        Page<Result> activeResults = resultRepository.findAllByDeletedFalse(Pageable.unpaged());
        assertThat(activeResults.getContent()).isEmpty();
    }

    @Test
    void shouldDeleteResultsWhenCourseIsDeleted() {
        Result result = resultRepository.save(new Result(student, course, Score.A));

        String mutation =
                """
                      mutation {
                                deleteCourse(id: %d)
                                }
                      """.formatted(course.getId());

        graphQlTester
                .document(mutation)
                .execute()
                .path("deleteCourse")
                .entity(Boolean.class)
                .isEqualTo(true);

        Optional<Result> deletedResult = resultRepository.findByIdAndDeletedFalse(result.getId());
        assertThat(deletedResult).isEmpty();

        Optional<Course> deletedCourse = courseRepository.findByIdAndDeletedFalse(course.getId());
        assertThat(deletedCourse).isEmpty();

        Page<Result> activeResults = resultRepository.findAllByDeletedFalse(Pageable.unpaged());
        assertThat(activeResults.getContent()).isEmpty();
    }
}
