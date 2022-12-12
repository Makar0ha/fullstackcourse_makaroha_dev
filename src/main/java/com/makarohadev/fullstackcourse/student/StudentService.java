package com.makarohadev.fullstackcourse.student;

import com.makarohadev.fullstackcourse.student.exception.BadRequestException;
import com.makarohadev.fullstackcourse.student.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void addStudent(Student student) {
        if (studentRepository.findByEmail(student.getEmail()).isPresent()){
            throw new BadRequestException("Student with email " + student.getEmail() + " exists");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        if(!studentRepository.existsById(studentId)){
            throw new NotFoundException("Student with id " + studentId + " doesn't exist");
        }
        studentRepository.deleteById(studentId);
    }

    public void editStudent(Student student) {
        Student studentChange = studentRepository.findById(student.getId()).orElse(null);
        if (studentChange == null) {
            throw new NotFoundException("Student with id " + student.getId() + " doesn't exist");
        }
        if(studentRepository.findByEmail(student.getEmail()).isPresent() && !student.getEmail().equals(studentChange.getEmail())){
            throw new  BadRequestException("Student with email " + student.getEmail() + " exists");
        }

        studentChange.setEmail(student.getEmail());
        studentChange.setName(student.getName());
        studentChange.setGender(student.getGender());
        studentRepository.save(studentChange);
    }
}
