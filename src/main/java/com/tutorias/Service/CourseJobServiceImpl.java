package com.tutorias.Service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tutorias.Repository.ICourseRepository;
import com.tutorias.Service.dto.CourseStatusJobDTO;

@Service
public class CourseJobServiceImpl implements ICourseJobService {

    @Autowired
    private ICourseRepository courseRepository;

    @Override
    public CourseStatusJobDTO updateCourseStatuses() {
        LocalDate today = LocalDate.now();

        int updatedToInProgress = this.courseRepository.updateStatusToInProgress(today);
        
        int updatedToInactive = this.courseRepository.updateStatusToInactive(today);

        return CourseStatusJobDTO.builder()
                .updatedToInProgress(updatedToInProgress)
                .updatedToInactive(updatedToInactive)
                .executionDate(today)
                .build();
    }
    
}
