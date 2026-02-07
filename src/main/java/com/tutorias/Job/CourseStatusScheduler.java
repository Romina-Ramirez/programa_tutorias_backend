package com.tutorias.Job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tutorias.Service.ICourseJobService;

@Component
public class CourseStatusScheduler {

    @Autowired
    private ICourseJobService courseJobService;

    @Scheduled(cron = "0 5 0 * * *")
    public void updateStatus() {
        courseJobService.updateCourseStatuses();
    }
    
}
