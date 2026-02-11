package com.tutorias.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorias.Service.ITutorService;
import com.tutorias.Service.dto.CourseCardDTO;
import com.tutorias.Service.dto.CourseTutorDetailDTO;
import com.tutorias.Service.dto.GradeDTO;
import com.tutorias.Service.dto.ReportDTO;
import com.tutorias.Service.dto.StudentDTO;

@RestController
@CrossOrigin
@RequestMapping(path = "/tutor")
public class TutorController {

    @Autowired
    private ITutorService tutorService;

    @GetMapping("/{userId}/courses")
    public ResponseEntity<List<CourseCardDTO>> getMyCourses(@PathVariable Integer userId) {
        return ResponseEntity.ok(tutorService.getMyCourses(userId));
    }

    @GetMapping("/{userId}/courses/{courseId}")
    public ResponseEntity<CourseTutorDetailDTO> getCourseDetail(@PathVariable Integer userId, @PathVariable Integer courseId) {
        return ResponseEntity.ok(tutorService.getCourseDetail(userId, courseId));
    }

    @GetMapping("/{userId}/courses/{courseId}/students")
    public ResponseEntity<List<StudentDTO>> getEnrolledStudents(@PathVariable Integer userId, @PathVariable Integer courseId) {
        return ResponseEntity.ok(tutorService.getEnrolledStudents(userId, courseId));
    }

    @PostMapping("/{userId}/courses/{courseId}/students/{studentId}/grades")
    public ResponseEntity<GradeDTO> addGrade(@PathVariable Integer userId, @PathVariable Integer courseId, @PathVariable Integer studentId, @RequestBody GradeDTO dto) {
        return ResponseEntity.ok(tutorService.addGrade(userId, courseId, studentId, dto));
    }

    @GetMapping("/{userId}/courses/{courseId}/grades/activity/{activity}")
    public ResponseEntity<List<GradeDTO>> getGradesByActivity(@PathVariable Integer userId, @PathVariable Integer courseId, @PathVariable String activity) {
        return ResponseEntity.ok(tutorService.getGradesByActivity(courseId, activity));
    }

    @PostMapping("/{userId}/courses/{courseId}/reports")
    public ResponseEntity<ReportDTO> addReport(@PathVariable Integer userId, @PathVariable Integer courseId, @RequestBody ReportDTO dto) {
        return ResponseEntity.ok(tutorService.addReport(userId, courseId, dto));
    }

    @GetMapping("/{userId}/courses/{courseId}/reports")
    public ResponseEntity<List<ReportDTO>> getReportsByCourse(@PathVariable Integer userId, @PathVariable Integer courseId) {
        return ResponseEntity.ok(tutorService.getReportsByCourse(userId, courseId));
    }
    
}