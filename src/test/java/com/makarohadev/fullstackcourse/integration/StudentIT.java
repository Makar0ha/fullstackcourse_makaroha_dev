package com.makarohadev.fullstackcourse.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.makarohadev.fullstackcourse.student.Gender;
import com.makarohadev.fullstackcourse.student.Student;
import com.makarohadev.fullstackcourse.student.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
public class StudentIT {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final StudentRepository studentRepository;

    private final Faker faker = new Faker();

    @Autowired
    public StudentIT(MockMvc mockMvc, ObjectMapper objectMapper, StudentRepository studentRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.studentRepository = studentRepository;
    }

    @Test
    void canRegisterNewStudent() throws Exception {
        //given
        int randomGenderPick = new Random().nextInt(Gender.values().length);
        String name = String.format("%s", faker.name().username().toLowerCase());
        Student student = new Student(
                name,
                name + "@mail.com",
                Gender.values()[randomGenderPick]);
        //when
        ResultActions resultActions = mockMvc
                .perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)));
        //then
        resultActions.andExpect(status().isOk());
        List<Student> students = studentRepository.findAll();
        assertThat(students)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(student);
    }

    @Test
    void canDeleteStudent() throws Exception {
        //given
        int randomGenderPick = new Random().nextInt(Gender.values().length);
        String name = String.format("%s", faker.name().username().toLowerCase());
        String email = name + "@mail.com";
        Student student = new Student(
                name,
                email,
                Gender.values()[randomGenderPick]);
        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk());
        MvcResult getStudentResult = mockMvc.perform(get("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = getStudentResult.getResponse().getContentAsString();

        List<Student> students = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {
                });

        long studentId = students.stream()
                .filter(s -> s.getEmail().equals(student.getEmail()))
                .map(Student::getId)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Student with email: " + email + " doesn't exist"));

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/students/" + studentId));
        //then
        resultActions.andExpect(status().isOk());
        boolean exists = studentRepository.existsById(studentId);
        assertThat(exists).isFalse();
    }
}
