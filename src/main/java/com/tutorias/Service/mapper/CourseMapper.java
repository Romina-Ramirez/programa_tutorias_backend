package com.tutorias.Service.mapper;

import org.springframework.stereotype.Component;

import com.tutorias.Repository.Model.Course;
import com.tutorias.Service.dto.CourseCardDTO;
import com.tutorias.Service.dto.CourseDTO;
import com.tutorias.Service.dto.CourseTutorDetailDTO;
import com.tutorias.Service.dto.StudentDTO;

@Component
public class CourseMapper {

    public CourseCardDTO convertToCourseCardDTO(Course course) {
        if (course == null) return null;

        String tutorName = null;
        if (course.getTutor() != null && course.getTutor().getUser() != null) {
            tutorName = course.getTutor().getUser().getName() + " " + course.getTutor().getUser().getLastName();
        }

        return CourseCardDTO.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .schedule(course.getSchedule())
                .subject(course.getSubject())
                .status(course.getStatus())
                .tutor(tutorName)
                .build();
    }

    public CourseDTO convertToCourseDTO(Course course) {
        if (course == null) return null;

        return CourseDTO.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .schedule(course.getSchedule())
                .subject(course.getSubject())
                .quota(course.getQuota())
                .status(course.getStatus())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .durationMinutes(course.getDurationMinutes())
                .tutorId(course.getTutor() != null ? course.getTutor().getId() : null)
                .build();
    }

    public CourseTutorDetailDTO convertToCourseTutorDetailDTO(Course course, java.util.List<StudentDTO> students) {
        if (course == null) return null;

        return CourseTutorDetailDTO.builder()
                .id(course.getId())
                .name(course.getName())
                .schedule(course.getSchedule())
                .status(course.getStatus())
                .students(students)
                .build();
    }
    
}
