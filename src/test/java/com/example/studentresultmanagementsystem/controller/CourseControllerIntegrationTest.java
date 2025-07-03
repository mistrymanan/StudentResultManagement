package com.example.studentresultmanagementsystem.controller;

import com.example.studentresultmanagementsystem.entity.Course;
import com.example.studentresultmanagementsystem.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureGraphQlTester
public class CourseControllerIntegrationTest {
    @Autowired
    private GraphQlTester graphQlTester;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
    }

    @Test
    void shouldCreateCourseSuccessfully() {
        String mutation =
                """
                    mutation {
                        createCourse(input: {name: "Java"}) {
                            id
                            name
                        }
                    }
                """;

        graphQlTester
                .document(mutation)
                .execute()
                .path("createCourse.name")
                .entity(String.class)
                .isEqualTo("Java");

        List<Course> courses = courseRepository.findAllByDeletedFalse(Pageable.unpaged()).getContent();
        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getName()).isEqualTo("Java");
    }

    @Test
    void shouldNotCreateDuplicateCourse() {
        courseRepository.save(new Course("Java"));

        String mutation =
                """
                    mutation {
                        createCourse(input: {name: "Java"}) {
                            id
                            name
                        }
                    }
                """;

        graphQlTester
                .document(mutation)
                .execute()
                .errors()
                .satisfy(
                        errors -> {
                            assertThat(errors).hasSize(1);
                            assertThat(errors.get(0).getMessage()).contains("Course Already Exists!");
                            assertThat(errors.get(0).getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
                        });
    }

    @Test
    void shouldFetchCourseById() {
        Course savedCourse = courseRepository.save(new Course("Programming"));
        String query =
                """
                    query {
                        courses(id: %d) {
                            content {
                                id
                                name
                            }
                            totalElements
                        }
                    }
                """.formatted(savedCourse.getId());

        graphQlTester
                .document(query)
                .execute()
                .path("courses.content[0].name")
                .entity(String.class)
                .isEqualTo("Programming");
    }

    @Test
    void shouldFetchAllCoursesWithPagination() {
        courseRepository.saveAll(
                List.of(new Course("Java"), new Course("Python"), new Course("Programming in C")));

        String query =
                """
                    query {
                        courses(page: 0, size: 2) {
                            content {
                                name
                            }
                            totalElements
                            totalPages
                        }
                    }
                """;

        graphQlTester
                .document(query)
                .execute()
                .path("courses.content")
                .entityList(Course.class)
                .hasSize(2)
                .path("courses.totalElements")
                .entity(Long.class)
                .isEqualTo(3L)
                .path("courses.totalPages")
                .entity(Integer.class)
                .isEqualTo(2);
    }

    @Test
    void shouldDeleteCourseSuccessfully() {
        Course savedCourse = courseRepository.save(new Course("Java"));

        String mutation =
                """
                    mutation {
                        deleteCourse(id: %d)
                    }
                """.formatted(savedCourse.getId());

        graphQlTester
                .document(mutation)
                .execute()
                .path("deleteCourse")
                .entity(Boolean.class)
                .isEqualTo(true);

        assertThat(courseRepository.findByIdAndDeletedFalse(savedCourse.getId())).isEmpty();
    }

    @Test
    void shouldNotDeleteNonExistentCourse() {
        String mutation =
                """
                    mutation {
                        deleteCourse(id: 11)
                    }
                """;

        graphQlTester
                .document(mutation)
                .execute()
                .errors()
                .satisfy(
                        errors -> {
                            assertThat(errors).hasSize(1);
                            assertThat(errors.get(0).getMessage()).contains("Course Not Found!");
                            assertThat(errors.get(0).getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
                        });
    }
}
