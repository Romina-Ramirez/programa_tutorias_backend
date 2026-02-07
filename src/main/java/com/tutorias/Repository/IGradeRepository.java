package com.tutorias.Repository;

import java.util.List;
import java.util.Optional;

import com.tutorias.Repository.Model.Grade;

public interface IGradeRepository {

    public Optional<Grade> findById(Integer id);

    public Grade save(Grade grade);

    public Grade update(Grade grade);

    public boolean softDelete(Integer id);

    public List<Grade> findByCourseAndStudent(Integer courseId, Integer studentId);

    public boolean existsByCourseAndStudent(Integer courseId, Integer studentId);

    public List<Grade> findByCourse(Integer courseId);

    public List<Grade> findByCourseAndActivity(Integer courseId, String activity);

    public boolean existsByCourseStudentAndActivity(Integer courseId, Integer studentId, String activity);

    public List<String> findDistinctActivitiesByCourse(Integer courseId);
}
