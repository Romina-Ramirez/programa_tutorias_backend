package com.tutorias.Repository;

import java.util.List;
import java.util.Optional;

import com.tutorias.Repository.Model.Student;

public interface IStudentRepository {

    public Optional<Student> findById(Integer id);

    public Optional<Student> findByUserId(Integer userId);

    public List<Student> findStudentsByCourseId(Integer courseId);

    public boolean existsById(Integer id);

    public Student save(Student student);

    public Student update(Student student);

    public boolean softDelete(Integer id);

    public boolean hardDelete(Integer id);

    public boolean hasAnyEnrollments(Integer studentId);
}
