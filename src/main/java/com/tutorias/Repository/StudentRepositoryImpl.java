package com.tutorias.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.tutorias.Repository.Model.Student;

@Repository
@Transactional
public class StudentRepositoryImpl implements IStudentRepository {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Student> findById(Integer id) {
        return Optional.ofNullable(this.entityManager.find(Student.class, id));
    }

    @Override
    public Optional<Student> findByUserId(Integer userId) {
        List<Student> students = entityManager.createQuery(
                        "SELECT s FROM Student s WHERE s.user.id = :userId",
                        Student.class
                )
                .setParameter("userId", userId)
                .setMaxResults(1)
                .getResultList();

        return students.isEmpty() ? Optional.empty() : Optional.of(students.get(0));
    }

    @Override
    public List<Student> findStudentsByCourseId(Integer courseId) {
        return this.entityManager.createQuery(
                        "SELECT s FROM Student s JOIN s.courses WHERE c.id = :courseId",
                        Student.class
                )
                .getResultList();
    }

    @Override
    public boolean existsById(Integer id) {
        Long count = this.entityManager.createQuery(
                        "SELECT COUNT(s.id) FROM Student s WHERE s.id = :id",
                        Long.class
                )
                .setParameter("id", id)
                .getSingleResult();

        return count != null && count > 0;
    }

    @Override
    public Student save(Student student) {
        this.entityManager.persist(student);
        return student;
    }

    @Override
    public Student update(Student student) {
        return this.entityManager.merge(student);
    }

    @Override
    public boolean softDelete(Integer id) {
        int updated = this.entityManager.createQuery(
                        "UPDATE Student s SET s.isDeleted = true WHERE s.id = :id AND s.isDeleted = false"
                )
                .setParameter("id", id)
                .executeUpdate();

        return updated == 1;
    }

    @Override
    public boolean hardDelete(Integer id) {
        Student student = this.entityManager.find(Student.class, id);
        if (student == null) return false;

        this.entityManager.remove(student);
        return true;
    }

    @Override
    public boolean hasAnyEnrollments(Integer studentId) {
        Number count = (Number) this.entityManager.createNativeQuery(
                        "SELECT COUNT(*) FROM course_student cs WHERE cs.stud_id = :studentId"
                )
                .setParameter("studentId", studentId)
                .getSingleResult();

        return count != null && count.longValue() > 0;
    }
}
