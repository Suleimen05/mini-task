package com.example.university.service;

import com.example.university.entity.Course;
import com.example.university.entity.Lesson;
import com.example.university.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    /**
     * Save Course
     */
    @Transactional
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    /**
     * Task 3.3: Add Lessons to Course
     * Saving Course automatically saves all related Lesson entities (cascade = ALL)
     */
    @Transactional
    public Course addLessonToCourse(Long courseId, Lesson lesson) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        // Bidirectional link
        course.addLesson(lesson);

        return courseRepository.save(course);
    }

    /**
     * Get Course by ID
     */
    @Transactional(readOnly = true)
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));
    }
}
