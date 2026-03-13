package com.example.university.service;

import com.example.university.entity.Course;
import com.example.university.entity.Student;
import com.example.university.repository.CourseRepository;
import com.example.university.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    /**
     * Task 3.1: Save Student with Profile
     * Saving Student automatically saves StudentProfile (cascade = ALL)
     */
    @Transactional
    public Student saveStudent(Student student) {
        // Ensure bidirectional link is set
        if (student.getProfile() != null) {
            student.getProfile().setStudent(student);
        }
        return studentRepository.save(student);
    }

    /**
     * Task 3.2: Assign Student to Course
     * Maintains bidirectional Many-to-Many relationship
     */
    @Transactional
    public Student assignStudentToCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        // Bidirectional: add to both sides
        student.getCourses().add(course);
        course.getStudents().add(student);

        return studentRepository.save(student);
    }

    /**
     * Get Student by ID
     */
    @Transactional(readOnly = true)
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));
    }

    /**
     * Task 3.4: Delete Student
     * Profile is deleted automatically (cascade + orphanRemoval)
     * Course data remains intact (no cascade on ManyToMany)
     */
    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));

        // Remove student from all courses (clean up join table)
        for (Course course : student.getCourses()) {
            course.getStudents().remove(student);
        }
        student.getCourses().clear();

        studentRepository.delete(student);
    }
}
