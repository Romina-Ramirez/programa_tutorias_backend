package com.tutorias.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import com.tutorias.Repository.Model.Grade;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class GradeRepositoryImpl implements IGradeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Grade> findById(Integer id) {
        return Optional.ofNullable(this.entityManager.find(Grade.class, id));
    }

    @Override
    public Grade save(Grade grade) {
        this.entityManager.persist(grade);
        return grade;
    }

    @Override
    public Grade update(Grade grade) {
        return this.entityManager.merge(grade);
    }

    @Override
    public boolean softDelete(Integer id) {
        int updated = this.entityManager.createQuery(
                        "UPDATE Grade g SET g.isDeleted = true WHERE g.id = :id AND g.isDeleted = false"
                )
                .setParameter("id", id)
                .executeUpdate();

        return updated == 1;
    }

    @Override
    public List<Grade> findByCourseAndStudent(Integer courseId, Integer studentId) {
        return this.entityManager.createQuery(
                        "SELECT g FROM Grade g WHERE g.isDeleted = false AND g.course.id = :courseId AND g.student.id = :studentId ORDER BY g.id ASC",
                        Grade.class
                )
                .setParameter("courseId", courseId)
                .setParameter("studentId", studentId)
                .getResultList();
    }

    @Override
    public boolean existsByCourseAndStudent(Integer courseId, Integer studentId) {
        Long count = this.entityManager.createQuery(
                        "SELECT COUNT(g.id) FROM Grade g WHERE g.isDeleted = false AND g.course.id = :courseId AND g.student.id = :studentId",
                        Long.class
                )
                .setParameter("courseId", courseId)
                .setParameter("studentId", studentId)
                .getSingleResult();

        return count != null && count > 0;
    }

    @Override
    public List<Grade> findByCourse(Integer courseId) {
        return this.entityManager.createQuery(
                        "SELECT g FROM Grade g WHERE g.isDeleted = false AND g.course.id = :courseId ORDER BY g.activity ASC, g.id ASC",
                        Grade.class
                )
                .setParameter("courseId", courseId)
                .getResultList();
    }

    @Override
    public List<Grade> findByCourseAndActivity(Integer courseId, String activity) {
        return this.entityManager.createQuery(
                        "SELECT g FROM Grade g WHERE g.isDeleted = false AND g.course.id = :courseId AND LOWER(g.activity) = LOWER(:activity) ORDER BY g.id ASC",
                        Grade.class
                )
                .setParameter("courseId", courseId)
                .setParameter("activity", activity)
                .getResultList();
    }

    @Override
    public boolean existsByCourseStudentAndActivity(Integer courseId, Integer studentId, String activity) {
        Long count = this.entityManager.createQuery(
                        "SELECT COUNT(g.id) FROM Grade g WHERE g.isDeleted = false AND g.course.id = :courseId AND g.student.id = :studentId AND LOWER(g.activity) = LOWER(:activity)",
                        Long.class
                )
                .setParameter("courseId", courseId)
                .setParameter("studentId", studentId)
                .setParameter("activity", activity)
                .getSingleResult();

        return count != null && count > 0;
    }

    @Override
    public List<String> findDistinctActivitiesByCourse(Integer courseId) {
        return this.entityManager.createQuery(
                        "SELECT DISTINCT g.activity FROM Grade g WHERE g.isDeleted=false AND g.course.id=:courseId",
                        String.class
                )
                .setParameter("courseId", courseId)
                .getResultList();
    }
}
