package com.tutorias.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorias.Service.ICourseJobService;
import com.tutorias.Service.dto.CourseStatusJobDTO;

@RestController
@CrossOrigin
@RequestMapping(path = "/job")
public class JobController {

    @Autowired
    private ICourseJobService courseJobService;

    @PostMapping("/courses/status")
    public ResponseEntity<CourseStatusJobDTO> updateCourseStatuses() {
        return ResponseEntity.ok(courseJobService.updateCourseStatuses());
    }
    
}
