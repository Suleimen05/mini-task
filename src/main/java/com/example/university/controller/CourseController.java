package com.example.university.controller;

import com.example.university.entity.Course;
import com.example.university.entity.Lesson;
import com.example.university.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    /**
     * POST /courses
     * Create a new Course
     */
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course saved = courseService.saveCourse(course);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * POST /courses/{courseId}/lessons
     * Add a Lesson to a Course (cascading save)
     */
    @PostMapping("/{courseId}/lessons")
    public ResponseEntity<Course> addLesson(
            @PathVariable Long courseId,
            @RequestBody Lesson lesson) {
        Course updated = courseService.addLessonToCourse(courseId, lesson);
        return ResponseEntity.ok(updated);
    }

    /**
     * GET /courses/{id}
     * Retrieve a Course by ID (with Lessons)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }
}
