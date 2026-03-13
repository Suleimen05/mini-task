package com.example.university.controller;

import com.example.university.entity.Student;
import com.example.university.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    /**
     * POST /students
     * Create a new Student (with optional Profile via cascade)
     */
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student saved = studentService.saveStudent(student);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * POST /students/{studentId}/courses/{courseId}
     * Assign a Student to a Course (Many-to-Many)
     */
    @PostMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<Student> assignToCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        Student updated = studentService.assignStudentToCourse(studentId, courseId);
        return ResponseEntity.ok(updated);
    }

    /**
     * GET /students/{id}
     * Retrieve a Student by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    /**
     * DELETE /students/{id}
     * Delete a Student (Profile cascades, Courses remain)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
