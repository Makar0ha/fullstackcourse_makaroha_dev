package com.makarohadev.fullstackcourse.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @AfterEach
    void tearDown() {
        studentRepository.deleteAll();
    }

    @Test
    void itShouldFindByEmail() {
        //given
        String email = "pequena@gmail.com";
        Student student = new Student("Pequena", email, Gender.OTHER);
        studentRepository.save(student);
        // when
        boolean present = studentRepository.findByEmail(email).isPresent();
        //then
        assertThat(present).isTrue();
    }

    @Test
    void itShouldCheckIfEmailNotExists() {
        //given
        String email = "pequena@gmail.com";
        // when
        boolean present = studentRepository.findByEmail(email).isPresent();
        //then
        assertThat(present).isFalse();
    }
}