package com.makarohadev.fullstackcourse.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    @GetMapping
    public List<Student> getAllStudents(){
        List<Student> students = Arrays.asList(
                new Student(1l, "Maks", "maks@makaroha.com", Gender.MALE),
                new Student(2l, "Mary", "mary@makaroha.com", Gender.FEMALE),
                new Student(3l, "Pequena", "peqa@makaroha.com", Gender.OTHER)
        );
        return students;
    }
}
