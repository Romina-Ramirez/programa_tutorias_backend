package com.tutorias.Service;

import java.util.List;

import com.tutorias.Service.dto.CourseCardDTO;
import com.tutorias.Service.dto.CourseTutorDetailDTO;
import com.tutorias.Service.dto.GradeDTO;
import com.tutorias.Service.dto.ReportDTO;
import com.tutorias.Service.dto.StudentDTO;
import com.tutorias.Service.dto.TutorDTO;

public interface ITutorService {

    // Cursos 
    public List<CourseCardDTO> getMyCourses(Integer userId);

    public CourseTutorDetailDTO getCourseDetail(Integer userId, Integer courseId);

    public List<StudentDTO> getEnrolledStudents(Integer userId, Integer courseId);

    // Calificaciones
    public GradeDTO addGrade(Integer userId, Integer courseId, Integer studentId, GradeDTO dto);

    public List<GradeDTO> getGradesByActivity(Integer courseId, String activity);

    public List<GradeDTO> getGradesByStudent(Integer courseId, Integer studentId);

    // Reportes
    public ReportDTO addReport(Integer userId, Integer courseId, ReportDTO dto);

    public List<ReportDTO> getReportsByCourse(Integer userId, Integer courseId);
    
    // Perfil
    public TutorDTO getProfile(Integer userId);
    
    public TutorDTO updateProfile(Integer userId, TutorDTO dto);
}
