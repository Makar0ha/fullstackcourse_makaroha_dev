package com.makarohadev.fullstackcourse.student;

import com.makarohadev.fullstackcourse.student.exception.BadRequestException;
import com.makarohadev.fullstackcourse.student.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository);
    }

    @Test
    void getAllStudents() {
        //when
        studentService.getAllStudents();
        //then
        verify(studentRepository).findAll();
    }

    @Test
    void addStudent() {
        //given
        Student student = new Student("Pequena", "pequena@gmail.com", Gender.OTHER);
        //when
        studentService.addStudent(student);
        //then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student captureStudent = studentArgumentCaptor.getValue();
        assertThat(captureStudent).isEqualTo(student);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        //given
        Student student = new Student("Pequena", "pequena@gmail.com", Gender.OTHER);
        given(studentRepository.findByEmail(student.getEmail()))
                .willReturn(Optional.of(student));
        //when
        //then
        assertThatThrownBy(() -> studentService.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Student with email " + student.getEmail() + " exists");
        verify(studentRepository, never()).save(any());
    }

    @Test
    void deleteStudent() {
        //given
        long studentId = 7;
        given(studentRepository.existsById(studentId))
                .willReturn(true);
        //when
        studentService.deleteStudent(studentId);
        //then
        verify(studentRepository).deleteById(studentId);
    }

    @Test
    void willThrowWhenDeleteStudentNotExists() {
        //given
        long studentId = 7;
        given(studentRepository.existsById(studentId))
                .willReturn(false);
        //when
        //then
        assertThatThrownBy(() -> studentService.deleteStudent(studentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Student with id " + studentId + " doesn't exist");
        verify(studentRepository, never()).deleteById(any());
    }
    @Test
    void canEditStudent() {
        //given
        long studentId = 7;
        Student student = new Student(studentId,"Pequena", "pequena@gmail.com", Gender.OTHER);
        Student changedStudent = new Student(studentId,"Pequena123", "pequena@gmail.com123", Gender.MALE);

        given(studentRepository.findById(studentId))
                .willReturn(Optional.of(student));
        given(studentRepository.findByEmail(changedStudent.getEmail()))
                .willReturn(Optional.empty());
        //when
        studentService.editStudent(changedStudent);
        //then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student captureStudent = studentArgumentCaptor.getValue();
        assertThat(captureStudent).isEqualTo(changedStudent);
    }

    @Test
    void cantEditStudentNotExists() {
        //given
        long studentId = 7;
        Student student = new Student(studentId,"Pequena4", "pequena4@gmail.com", Gender.OTHER);

        given(studentRepository.findById(studentId))
                .willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> studentService.editStudent(student))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Student with id " + studentId + " doesn't exist");
        verify(studentRepository, never()).save(any());
    }

    @Test
    void cantEditStudentEmailExists() {
        //given
        long studentId = 7;
        Student student = new Student(studentId,"Pequena", "pequena@gmail.com", Gender.OTHER);
        Student otherStudent = new Student("Pequena2", "pequena123@gmail.com", Gender.FEMALE);
        Student changedStudent = new Student(studentId,"Pequena123", "pequena123@gmail.com", Gender.MALE);

        given(studentRepository.findById(studentId))
                .willReturn(Optional.of(student));
        given(studentRepository.findByEmail(changedStudent.getEmail()))
                .willReturn(Optional.of(otherStudent));
        //when
        //then
        assertThatThrownBy(() -> studentService.editStudent(changedStudent))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Student with email " + changedStudent.getEmail() + " exists");
        verify(studentRepository, never()).save(any());
    }
}