package com.example.studentresultmanagementsystem.controller;

import com.example.studentresultmanagementsystem.entity.Student;
import com.example.studentresultmanagementsystem.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureGraphQlTester
public class StudentControllerIntegrationTest {
    @Autowired
    private GraphQlTester graphQlTester;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    void shouldAddStudentSuccessfully() {
        String mutation =
                """
                mutation {
                    addStudent(input: {
                        firstName: "Manan",
                        familyName: "Mistry",
                        email: "mananmistry10@gmail.com",
                        dateOfBirth: "12/28/1999"
                    }) {
                        id
                        firstName
                        familyName
                        email
                        dateOfBirth
                    }
                }
            """;

        graphQlTester
                .document(mutation)
                .execute()
                .path("addStudent.firstName")
                .entity(String.class)
                .isEqualTo("Manan")
                .path("addStudent.familyName")
                .entity(String.class)
                .isEqualTo("Mistry")
                .path("addStudent.email")
                .entity(String.class)
                .isEqualTo("mananmistry10@gmail.com")
                .path("addStudent.dateOfBirth")
                .entity(String.class)
                .isEqualTo("12/28/1999");

        List<Student> students =
                studentRepository.findAllByDeletedFalse(Pageable.unpaged()).getContent();
        assertThat(students).hasSize(1);
        assertThat(students.get(0).getFirstName()).isEqualTo("Manan");
        assertThat(students.get(0).getFamilyName()).isEqualTo("Mistry");
        assertThat(students.get(0).getEmail()).isEqualTo("mananmistry10@gmail.com");
        assertThat(students.get(0).getDateOfBirth().toString())
                .isEqualTo("1999-12-28");
    }

    @Test
    void shouldNotAddStudentWithInvalidEmailDomain() {
        String mutation =
                """
                  mutation {
                      addStudent(input: {
                          firstName: "Manan",
                          familyName: "Mistry",
                          email: "mananmistry10@hotmail.com",
                          dateOfBirth: "12/28/1999"
                      }) {
                          id
                          firstName
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
                            assertThat(errors.get(0).getMessage())
                                    .contains(
                                            "Email domain is not allowed. Only Gmail.com and Yahoo.com are permitted.");
                        });

        assertThat(studentRepository.findAllByDeletedFalse(Pageable.unpaged())).isEmpty();
    }
    @Test
    void shouldNotAddStudentWithInvalidDateOfBirth() {
        String mutation =
                """
                  mutation {
                      addStudent(input: {
                          firstName: "Manan",
                          familyName: "Mistry",
                          email: "mananmistry@gmail.com",
                          dateOfBirth: "12/28/2015"
                      }) {
                          id
                          firstName
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
                            assertThat(errors.get(0).getMessage())
                                    .contains("Student must be at least 10 years old");
                        });
        assertThat(studentRepository.findAllByDeletedFalse(Pageable.unpaged())).isEmpty();
    }

    @Test
    void shouldNotAddStudentWithMissingFields() {
        String mutation = """
            mutation {
                addStudent(input: {
                    firstName: "Manan",
                    email: "mananmistry10@gmail.com"
                }) {
                    id
                    firstName
                }
            }
        """;

        graphQlTester.document(mutation)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).hasSize(1);
                    assertThat(errors.get(0).getMessage()).contains("missing required fields '[familyName, dateOfBirth]'");
                });

        assertThat(studentRepository.findAllByDeletedFalse(Pageable.unpaged())).isEmpty();
    }
}
