package com.makarohadev.fullstackcourse.student;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
    public void addStudent(@RequestBody Student student){
        studentService.addStudent(student);
    }
    @DeleteMapping("{studentId}")
    public void deleteStudent(@PathVariable Long studentId){
        System.out.println(studentId);
        studentService.deleteStudent(studentId);
    }
    @PutMapping
    public List<Student> editStudent(@RequestBody Student student){
        return studentService.editStudent(student);
    }

}
