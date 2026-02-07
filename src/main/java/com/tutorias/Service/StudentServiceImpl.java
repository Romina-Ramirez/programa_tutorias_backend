package com.tutorias.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tutorias.Repository.ICourseRepository;
import com.tutorias.Repository.IGradeRepository;
import com.tutorias.Repository.IStudentRepository;
import com.tutorias.Repository.Model.Course;
import com.tutorias.Repository.Model.Student;
import com.tutorias.Service.dto.CourseCardDTO;
import com.tutorias.Service.dto.GradeDTO;
import com.tutorias.Service.dto.StudentGradesDTO;
import com.tutorias.Service.exception.NotFoundException;
import com.tutorias.Service.mapper.CourseMapper;
import com.tutorias.Service.mapper.InteractionMapper;

@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired 
    private IStudentRepository studentRepository;

    @Autowired 
    private ICourseRepository courseRepository;

    @Autowired 
    private IGradeRepository gradeRepository;

    @Autowired 
    private CourseMapper courseMapper;

    @Autowired 
    private InteractionMapper interactionMapper;

    // Cursos
    @Override
    public List<CourseCardDTO> getAvailableCourses() {
        return this.courseRepository.findAvailableCourses().stream()
                .map(this.courseMapper::convertToCourseCardDTO)
                .toList();
    }

    @Override
    public List<CourseCardDTO> getMyCourses(Integer userId) {
        Student student = this.studentRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Estudiante no encontrado para este usuario"));

        return this.courseRepository.findByStudentId(student.getId()).stream()
                .map(this.courseMapper::convertToCourseCardDTO)
                .toList();
    }

    @Override
    public boolean enroll(Integer userId, Integer courseId) {
        Student student = this.studentRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Estudiante no encontrado para este usuario"));

        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Curso no encontrado"));

        long enrolled = this.courseRepository.countEnrolledStudents(courseId);

        if (enrolled >= course.getQuota()) {
            throw new RuntimeException("El curso ya está lleno");
        }

        boolean ok = this.courseRepository.enrollStudent(courseId, student.getId());

        if (!ok) {
            throw new RuntimeException("No se pudo inscribir, intenta nuevamente");
        }

        return ok;
    }

    @Override
    public CourseCardDTO getCourseDetail(Integer userId, Integer courseId) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Curso no encontrado"));

        return this.courseMapper.convertToCourseCardDTO(course);
    }
    
    // Calificaciones
    @Override
    public StudentGradesDTO getMyGrades(Integer userId, Integer courseId) {
        Student student = this.studentRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Estudiante no encontrado para este usuario"));

        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Curso no encontrado"));

        if (!this.gradeRepository.existsByCourseAndStudent(courseId, student.getId())) {
            throw new NotFoundException("Aún no hay calificaciones para este curso");
        }

        List<GradeDTO> grades = this.gradeRepository.findByCourseAndStudent(courseId, student.getId()).stream()
                .map(this.interactionMapper::convertToGradeDTO)
                .toList();

        return StudentGradesDTO.builder()
                .courseId(course.getId())
                .courseName(course.getName())
                .grades(grades)
                .build();
    }

}
