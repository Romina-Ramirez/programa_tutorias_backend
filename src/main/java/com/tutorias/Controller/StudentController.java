package com.tutorias.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorias.Service.IStudentService;
import com.tutorias.Service.dto.CourseCardDTO;
import com.tutorias.Service.dto.StudentGradesDTO;

@RestController
@CrossOrigin
@RequestMapping(path = "/student")
public class StudentController {

    @Autowired
    private IStudentService studentService;

    @GetMapping("/{userId}/courses/available")
    public ResponseEntity<List<CourseCardDTO>> getAvailableCourses(@PathVariable Integer userId) {
        return ResponseEntity.ok(studentService.getAvailableCourses());
    }

    @GetMapping("/{userId}/courses")
    public ResponseEntity<List<CourseCardDTO>> getMyCourses(@PathVariable Integer userId) {
        return ResponseEntity.ok(studentService.getMyCourses(userId));
    }

    @GetMapping("/{userId}/courses/{courseId}")
    public ResponseEntity<CourseCardDTO> getCourseDetail(@PathVariable Integer userId, @PathVariable Integer courseId) {
        return ResponseEntity.ok(studentService.getCourseDetail(userId, courseId));
    }

    @PostMapping("/{userId}/courses/{courseId}/enroll")
    public ResponseEntity<Boolean> enroll(@PathVariable Integer userId, @PathVariable Integer courseId) {
        return ResponseEntity.ok(studentService.enroll(userId, courseId));
    }

    @GetMapping("/{userId}/courses/{courseId}/grades")
    public ResponseEntity<StudentGradesDTO> getMyGrades(@PathVariable Integer userId, @PathVariable Integer courseId) {
        return ResponseEntity.ok(studentService.getMyGrades(userId, courseId));
    }
    
}