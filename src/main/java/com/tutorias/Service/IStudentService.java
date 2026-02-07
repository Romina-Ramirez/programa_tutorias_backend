package com.tutorias.Service;

import java.util.List;

import com.tutorias.Service.dto.CourseCardDTO;
import com.tutorias.Service.dto.StudentGradesDTO;

public interface IStudentService {

    // Cursos
    public List<CourseCardDTO> getAvailableCourses();

    public List<CourseCardDTO> getMyCourses(Integer userId);

    public boolean enroll(Integer userId, Integer courseId);

    public CourseCardDTO getCourseDetail(Integer userId, Integer courseId);

    // Calificaciones
    public StudentGradesDTO getMyGrades(Integer userId, Integer courseId);
    
}
