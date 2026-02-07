package com.tutorias.Repository;

import com.tutorias.Repository.Model.Course;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ICourseRepository {

    public Optional<Course> findById(Integer id);

    public Course save(Course course);

    public Course update(Course course);

    public boolean isCourseEditable(Integer id);

    public List<Course> findAvailableCourses();

    public List<Course> findByStudentId(Integer studentId);

    public List<Course> findByTutorId(Integer tutorId);

    public List<Course> findByAdminId(Integer adminId);

    public boolean isStudentEnrolled(Integer courseId, Integer studentId);

    public long countEnrolledStudents(Integer id);

    public boolean enrollStudent(Integer courseId, Integer studentId);

    public boolean softDelete(Integer id);

    public boolean hardDelete(Integer id);

    public boolean hasAnyForums(Integer id);

    public boolean hasAnyGrades(Integer id);

    public boolean hasAnyReports(Integer id);

    public int updateStatusToInProgress(LocalDate today);

    public int updateStatusToInactive(LocalDate today);
}
