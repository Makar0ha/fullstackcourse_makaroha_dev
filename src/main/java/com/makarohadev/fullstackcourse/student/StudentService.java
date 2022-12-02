package com.makarohadev.fullstackcourse.student;

import com.makarohadev.fullstackcourse.student.exception.BadRequestException;
import com.makarohadev.fullstackcourse.student.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void addStudent(Student student) {
        System.out.println(student.getEmail());
        if (studentRepository.findByEmail(student.getEmail()).isPresent()){
            System.out.println(1);
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

    public List<Student> editStudent(Student student) {
        Student studentChange = studentRepository.findById(student.getId()).orElse(null);
        if (studentChange == null) {
            throw new EntityExistsException("Student with id " + student.getId() + " doesn't exist");
        }
        studentRepository.findByEmail(student.getEmail())
                .orElseThrow(() -> new IllegalStateException("Student with email " + student.getEmail() + " exists"));

        studentChange.setEmail(student.getEmail());
        studentChange.setName(student.getName());
        studentChange.setGender(student.getGender());
        studentRepository.save(studentChange);
        return studentRepository.findAll();
    }
}
