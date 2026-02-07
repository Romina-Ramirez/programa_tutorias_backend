package com.tutorias.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorias.Service.IAdminService;
import com.tutorias.Service.dto.CourseDTO;
import com.tutorias.Service.dto.GeneralReportDTO;
import com.tutorias.Service.dto.ProfileDTO;
import com.tutorias.Service.dto.TutorDTO;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@CrossOrigin
@RequestMapping(path = "/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    // Tutores
    @PostMapping("/{adminUserId}/tutors")
    public ResponseEntity<ProfileDTO> createTutor(@PathVariable Integer adminUserId, @RequestBody TutorDTO dto) {
        return ResponseEntity.ok(adminService.createTutor(adminUserId, dto));
    }

    @PutMapping("/{adminUserId}/tutors/{tutorId}/activate")
    public ResponseEntity<Boolean> activate(@PathVariable Integer adminUserId, @PathVariable Integer tutorId) {
        return ResponseEntity.ok(adminService.sendEmailNewTutor(tutorId));
    }

    @GetMapping("/{adminUserId}/tutors")
    public ResponseEntity<List<ProfileDTO>> listMyTutors(@PathVariable Integer adminUserId) {
        return ResponseEntity.ok(adminService.listMyTutors(adminUserId));
    }

    @PutMapping("/{adminUserId}/tutors/{tutorId}")
    public ResponseEntity<ProfileDTO> updateTutor(@PathVariable Integer adminUserId, @PathVariable Integer tutorId, @RequestBody TutorDTO dto) {
        return ResponseEntity.ok(adminService.updateTutor(adminUserId, tutorId, dto));
    }

    @PutMapping("/{adminUserId}/tutors/{tutorId}")
    public ResponseEntity<Boolean> deleteTutor(@PathVariable Integer adminUserId, @PathVariable Integer tutorId) {
        return ResponseEntity.ok(adminService.deleteTutor(adminUserId, tutorId));
    }

    // Cursos
    @PostMapping("/{adminUserId}/courses")
    public ResponseEntity<CourseDTO> createCourse(@PathVariable Integer adminUserId, @RequestBody CourseDTO dto) {
        return ResponseEntity.ok(adminService.createCourse(adminUserId, dto));
    }

    @GetMapping("/{adminUserId}/courses")
    public ResponseEntity<List<CourseDTO>> listMyCourses(@PathVariable Integer adminUserId) {
        return ResponseEntity.ok(adminService.listMyCourses(adminUserId));
    }

    @PutMapping("/{adminUserId}/courses/{courseId}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Integer adminUserId, @PathVariable Integer courseId, @RequestBody CourseDTO dto) {
        dto.setId(courseId); 
        return ResponseEntity.ok(adminService.updateCourse(adminUserId, dto));
    }

    @PutMapping("/{adminUserId}/courses/{courseId}")
    public ResponseEntity<Boolean> deleteCourse(@PathVariable Integer adminUserId, @PathVariable Integer courseId) {
        return ResponseEntity.ok(adminService.deleteCourse(adminUserId, courseId));
    }

    // Reporte general
    @GetMapping("/{adminUserId}/tutors/{tutorId}/general-report")
    public ResponseEntity<GeneralReportDTO> getTutorGeneralReport(@PathVariable Integer adminUserId, @PathVariable Integer tutorId) {
        return ResponseEntity.ok(adminService.getTutorGeneralReport(adminUserId, tutorId));
    }
}
