package com.tutorias.Service;

import java.security.SecureRandom;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tutorias.Repository.ICourseRepository;
import com.tutorias.Repository.IGeneralReportRepository;
import com.tutorias.Repository.IReportRepository;
import com.tutorias.Repository.ITutorRepository;
import com.tutorias.Repository.IUserRepository;
import com.tutorias.Repository.Enums.CourseStatus;
import com.tutorias.Repository.Enums.Role;
import com.tutorias.Repository.Model.Course;
import com.tutorias.Repository.Model.GeneralReport;
import com.tutorias.Repository.Model.Report;
import com.tutorias.Repository.Model.Tutor;
import com.tutorias.Repository.Model.User;
import com.tutorias.Service.dto.CourseDTO;
import com.tutorias.Service.dto.GeneralReportDTO;
import com.tutorias.Service.dto.ProfileDTO;
import com.tutorias.Service.dto.TutorDTO;
import com.tutorias.Service.mapper.CourseMapper;
import com.tutorias.Service.mapper.InteractionMapper;
import com.tutorias.Service.mapper.UserMapper;

import jakarta.mail.MessagingException;

import com.tutorias.Service.exception.EmailSendException;
import com.tutorias.Service.exception.NotFoundException;

@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired 
    private IUserRepository userRepository;

    @Autowired 
    private ITutorRepository tutorRepository;

    @Autowired 
    private ICourseRepository courseRepository;

    @Autowired 
    private IGeneralReportRepository generalReportRepository;

    @Autowired 
    private IReportRepository reportRepository;

    @Autowired 
    private UserMapper userMapper;

    @Autowired 
    private CourseMapper courseMapper;

    @Autowired 
    private InteractionMapper interactionMapper;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Tutores
    @Override
    public ProfileDTO createTutor(Integer adminUserId, TutorDTO dto) {

        if (this.userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }

        User user = User.builder()
                .name(dto.getName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(null)
                .role(Role.TUTOR)
                .isDeleted(false)
                .build();

        this.userRepository.save(user);

        Tutor tutor = Tutor.builder()
                .id(user.getId())
                .user(user)
                .career(dto.getCareer())
                .phone(dto.getPhone())
                .adminId(adminUserId)
                .minutesCompleted(0)
                .isDeleted(false)
                .availableSchedule(dto.getAvailableSchedule())
                .isActive(false)
                .build();

        this.tutorRepository.save(tutor);

        sendEmailNewTutor(user.getId());
        
        GeneralReport gr = GeneralReport.builder()
                .tutor(tutor)
                .observation(null)
                .isDeleted(false)
                .build();

        this.generalReportRepository.save(gr);

        return this.userMapper.convertToProfileDTO(user, tutor);
    }

    @Override
    public boolean sendEmailNewTutor(Integer tutorId) {
        User user = this.userRepository.findById(tutorId)
                .orElseThrow(() -> new NotFoundException("Tutor no encontrado"));

        String password = generateRandomPassword(8);
        String hashed = this.passwordEncoder.encode(password);

        boolean updated = this.userRepository.updatePassword(user.getEmail(), hashed);

        if (!updated) {
            throw new RuntimeException("No se pudo cambiar la contraseña");
        }

        boolean send = false;

        try {
            this.emailService.sendnewTutor(user.getEmail(), password);
            this.tutorRepository.activate(tutorId);
            send = true;
        } catch (MessagingException e) {
            throw new EmailSendException("No se pudo enviar el correo al tutor.");
        }

        return send;
    }

    @Override
    public List<ProfileDTO> listMyTutors(Integer adminUserId) {
        return this.tutorRepository.findByAdminId(adminUserId).stream()
                .map(t -> this.userMapper.convertToProfileDTO(t.getUser(), t))
                .toList();
    }

    @Override
    public ProfileDTO updateTutor(Integer adminUserId, Integer tutorId, TutorDTO dto) {
        Tutor tutor = this.tutorRepository.findById(tutorId)
                .orElseThrow(() -> new NotFoundException("Tutor no encontrado"));
        
        User user = tutor.getUser();
        
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getCareer() != null) tutor.setCareer(dto.getCareer());
        if (dto.getPhone() != null) tutor.setPhone(dto.getPhone());
        if (dto.getAvailableSchedule() != null) tutor.setAvailableSchedule(dto.getAvailableSchedule());

        Tutor updated = this.tutorRepository.update(tutor);
        this.userRepository.update(user);

        return this.userMapper.convertToProfileDTO(updated.getUser(), updated);
    }

    @Override
    public boolean deleteTutor(Integer adminUserId, Integer tutorId) {

        boolean tutorDeleted = this.tutorRepository.softDelete(tutorId);

        boolean userDeleted = this.userRepository.softDelete(tutorId);

        return tutorDeleted && userDeleted;
    }

    // Cursos
    @Override
    public CourseDTO createCourse(Integer adminUserId, CourseDTO dto) {
        Tutor tutor = this.tutorRepository.findById(dto.getTutorId())
                .orElseThrow(() -> new NotFoundException("Tutor no encontrado"));

        Course course = Course.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .schedule(dto.getSchedule())
                .subject(dto.getSubject())
                .quota(dto.getQuota())
                .status(CourseStatus.ACTIVE)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .durationMinutes(dto.getDurationMinutes())
                .tutor(tutor)
                .isDeleted(false)
                .build();

        this.courseRepository.save(course);
        
        return this.courseMapper.convertToCourseDTO(course);
    }

    @Override
    public List<CourseDTO> listMyCourses(Integer adminUserId) {
        return this.courseRepository.findByAdminId(adminUserId).stream()
                .map(this.courseMapper::convertToCourseDTO)
                .toList();
    }

    @Override
    public CourseDTO updateCourse(Integer adminUserId, CourseDTO dto) {
        Course course = this.courseRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Curso no encontrado"));

        if (dto.getName() != null) course.setName(dto.getName());
        if (dto.getDescription() != null) course.setDescription(dto.getDescription());
        if (dto.getSchedule() != null) course.setSchedule(dto.getSchedule());
        if (dto.getSubject() != null) course.setSubject(dto.getSubject());
        if (dto.getQuota() != null) course.setQuota(dto.getQuota());
        if (dto.getStartDate() != null) course.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) course.setEndDate(dto.getEndDate());
        if (dto.getDurationMinutes() != null) course.setDurationMinutes(dto.getDurationMinutes());

        Course updated = this.courseRepository.update(course);
        return this.courseMapper.convertToCourseDTO(updated);
    }

    @Override
    public boolean deleteCourse(Integer adminUserId, Integer courseId) {
        if (this.courseRepository.countEnrolledStudents(courseId) > 0) {
            return courseRepository.softDelete(courseId);
        }

        return courseRepository.hardDelete(courseId);
    }

    // Reporte general
    @Override
    public GeneralReportDTO getTutorGeneralReport(Integer adminUserId, Integer tutorId) {
        Tutor tutor = this.tutorRepository.findById(tutorId)
                .orElseThrow(() -> new NotFoundException("Tutor no encontrado"));

        String tutorName = tutor.getUser().getName() + " " + tutor.getUser().getLastName();;
   
        GeneralReport gr = generalReportRepository.findByTutorId(tutorId)
                .orElseThrow(() -> new NotFoundException("Reporte general no encontrado para este tutor"));

        List<Report> reports = this.reportRepository.findByGeneralReportId(gr.getId());

        return this.interactionMapper.convertToGeneralReportDTO(gr, tutorName, reports);
    }

    // Generar una contraseña aleatoria
    public static String generateRandomPassword(int length) {
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom RANDOM = new SecureRandom();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
    
}
