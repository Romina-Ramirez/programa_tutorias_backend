package com.tutorias.Service;

import java.util.List;

import com.tutorias.Service.dto.CourseDTO;
import com.tutorias.Service.dto.GeneralReportDTO;
import com.tutorias.Service.dto.ProfileDTO;
import com.tutorias.Service.dto.TutorDTO;

public interface IAdminService {

    // Tutores
    public ProfileDTO createTutor(Integer adminUserId, TutorDTO dto);

    public boolean sendEmailNewTutor(Integer tutorId);
    
    public List<ProfileDTO> listMyTutors(Integer adminUserId);

    public ProfileDTO updateTutor(Integer adminUserId, Integer tutorId, TutorDTO dto);

    public boolean deleteTutor(Integer adminUserId, Integer tutorId);

    // Cursos
    public CourseDTO createCourse(Integer adminUserId, CourseDTO dto);

    public List<CourseDTO> listMyCourses(Integer adminUserId);

    public CourseDTO updateCourse(Integer adminUserId, CourseDTO dto);

    public boolean deleteCourse(Integer adminUserId, Integer courseId);

    // Reporte general 
    public GeneralReportDTO getTutorGeneralReport(Integer adminUserId, Integer tutorId);

}
