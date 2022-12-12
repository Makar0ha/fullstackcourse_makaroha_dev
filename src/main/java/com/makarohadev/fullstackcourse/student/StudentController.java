package com.makarohadev.fullstackcourse.student;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/students")
@AllArgsConstructor
public class StudentController {

    private StudentService studentService;
    @GetMapping
    public List<Student> getAllStudents(){
        return studentService.getAllStudents();
    }
    @PostMapping
    public void addStudent(@Valid @RequestBody Student student){
        studentService.addStudent(student);
    }
    @DeleteMapping("{studentId}")
    public void deleteStudent(@PathVariable Long studentId){
        System.out.println(studentId);
        studentService.deleteStudent(studentId);
    }
    @PutMapping
    public void editStudent(@Valid @RequestBody Student student){
        System.out.println(student);
        studentService.editStudent(student);
    }

}
