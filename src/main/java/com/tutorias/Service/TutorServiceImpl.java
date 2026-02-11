package com.tutorias.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tutorias.Repository.ICourseRepository;
import com.tutorias.Repository.IGeneralReportRepository;
import com.tutorias.Repository.IGradeRepository;
import com.tutorias.Repository.IReportRepository;
import com.tutorias.Repository.IStudentRepository;
import com.tutorias.Repository.ITutorRepository;
import com.tutorias.Repository.Model.Course;
import com.tutorias.Repository.Model.GeneralReport;
import com.tutorias.Repository.Model.Grade;
import com.tutorias.Repository.Model.Report;
import com.tutorias.Repository.Model.Student;
import com.tutorias.Repository.Model.Tutor;
import com.tutorias.Service.dto.CourseCardDTO;
import com.tutorias.Service.dto.CourseTutorDetailDTO;
import com.tutorias.Service.dto.GradeDTO;
import com.tutorias.Service.dto.ReportDTO;
import com.tutorias.Service.dto.StudentDTO;
import com.tutorias.Service.exception.NotFoundException;
import com.tutorias.Service.mapper.CourseMapper;
import com.tutorias.Service.mapper.InteractionMapper;
import com.tutorias.Service.mapper.UserMapper;

@Service
public class TutorServiceImpl implements ITutorService {

    @Autowired 
    private ITutorRepository tutorRepository;

    @Autowired 
    private ICourseRepository courseRepository;

    @Autowired 
    private IStudentRepository studentRepository;

    @Autowired 
    private IGradeRepository gradeRepository;

    @Autowired 
    private IReportRepository reportRepository;

    @Autowired 
    private IGeneralReportRepository generalReportRepository;

    @Autowired 
    private CourseMapper courseMapper;

    @Autowired 
    private UserMapper userMapper;

    @Autowired 
    private InteractionMapper interactionMapper;

    @Override
    public List<CourseCardDTO> getMyCourses(Integer userId) {
        Tutor tutor = this.tutorRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Tutor no encontrado para este usuario"));

        return this.courseRepository.findByTutorId(tutor.getId()).stream()
                .map(this.courseMapper::convertToCourseCardDTO)
                .toList();
    }

    @Override
    public CourseTutorDetailDTO getCourseDetail(Integer userId, Integer courseId) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Curso no encontrado"));

        List<StudentDTO> students = course.getStudents() == null ? List.of()
                : course.getStudents().stream()
                    .filter(s -> !Boolean.TRUE.equals(s.getIsDeleted()))
                    .map(s -> this.userMapper.convertToStudentDTO(s.getUser()))
                    .toList();

        return CourseTutorDetailDTO.builder()
                .id(course.getId())
                .name(course.getName())
                .schedule(course.getSchedule())
                .status(course.getStatus())
                .students(students)
                .build();
    }

    @Override
    public List<StudentDTO> getEnrolledStudents(Integer userId, Integer courseId) {
        return this.studentRepository.findStudentsByCourseId(courseId).stream()
                .filter(s -> !Boolean.TRUE.equals(s.getIsDeleted()))
                .map(s -> this.userMapper.convertToStudentDTO(s.getUser()))
                .toList();
    }

    @Override
    public GradeDTO addGrade(Integer userId, Integer courseId, Integer studentId, GradeDTO dto) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Curso no encontrado"));

        Student student = this.studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));

        if (this.gradeRepository.existsByCourseStudentAndActivity(courseId, studentId, dto.getActivity())) {
            throw new RuntimeException("Ya existe una nota para esa actividad y estudiante");
        }

        Grade grade = Grade.builder()
                .activity(dto.getActivity())
                .qualification(dto.getQualification())
                .observations(dto.getObservations())
                .isDeleted(false)
                .course(course)
                .student(student)
                .build();

        this.gradeRepository.save(grade);

        return this.interactionMapper.convertToGradeDTO(grade);
    }

    @Override
    public List<GradeDTO> getGradesByActivity(Integer courseId, String activity) {
        return this.gradeRepository.findByCourseAndActivity(courseId, activity).stream()
                .map(this.interactionMapper::convertToGradeDTO)
                .toList();
    }

    @Override
    public ReportDTO addReport(Integer userId, Integer courseId, ReportDTO dto) {
        Tutor tutor = this.tutorRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Tutor no encontrado para este usuario"));

        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Curso no encontrado"));

        GeneralReport gr = this.generalReportRepository.findByTutorId(tutor.getId())
                .orElseThrow(() -> new NotFoundException("Reporte general no encontrado"));

        Report report = Report.builder()
                .createdAt(LocalDateTime.now())
                .activityDescription(dto.getActivityDescription())
                .minutesCompleted(dto.getMinutesCompleted())
                .course(course)
                .generalReport(gr)
                .build();

        this.reportRepository.save(report);

        tutor.setMinutesCompleted((tutor.getMinutesCompleted() == null ? 0 : tutor.getMinutesCompleted()) + dto.getMinutesCompleted());

        this.tutorRepository.update(tutor);

        return this.interactionMapper.convertToReportDTO(report);
    }

    @Override
    public List<ReportDTO> getReportsByCourse(Integer userId, Integer courseId) {
        return this.reportRepository.findByCourseId(courseId).stream()
                .map(this.interactionMapper::convertToReportDTO)
                .toList();
    }
}
