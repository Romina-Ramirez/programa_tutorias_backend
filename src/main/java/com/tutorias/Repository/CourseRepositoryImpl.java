package com.tutorias.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.tutorias.Repository.Enums.CourseStatus;
import com.tutorias.Repository.Model.Course;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class CourseRepositoryImpl implements ICourseRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Course> findById(Integer id) {
        return Optional.ofNullable(this.entityManager.find(Course.class, id));
    }

    @Override
    public Course save(Course course) {
        this.entityManager.persist(course);
        return course;
    }

    @Override
    public Course update(Course course) {
        return this.entityManager.merge(course);
    }

    @Override
    public boolean isCourseEditable(Integer id) {
        Long count = this.entityManager.createQuery(
                        "SELECT COUNT(c.id) FROM Course c WHERE c.id = :id AND c.status = :status",
                        Long.class
                )
                .setParameter("id", id)
                .setParameter("status", CourseStatus.ACTIVE)
                .getSingleResult();

        return count != null && count > 0;
    }

    @Override
    public List<Course> findAvailableCourses() {
        return this.entityManager.createQuery(
                        "SELECT c FROM Course c WHERE c.isDeleted = false AND c.status = :status AND c.tutor.isDeleted = false " +
                        "AND (SELECT COUNT(s.id) FROM c.students s) < c.quota ORDER BY c.startDate ASC",
                        Course.class
                )
                .setParameter("status", CourseStatus.ACTIVE)
                .getResultList();
    }

    @Override
    public List<Course> findByStudentId(Integer studentId) {
        return this.entityManager.createQuery(
                        "SELECT c FROM Course c JOIN c.students s WHERE c.isDeleted = false AND s.id = :studentId ORDER BY c.startDate DESC",
                        Course.class
                )
                .setParameter("studentId", studentId)
                .getResultList();
    }

    @Override
    public List<Course> findByTutorId(Integer tutorId) {
        return this.entityManager.createQuery(
                        "SELECT c FROM Course c WHERE c.isDeleted = false AND c.tutor.id = :tutorId ORDER BY c.startDate DESC",
                        Course.class
                )
                .setParameter("tutorId", tutorId)
                .getResultList();
    }

    @Override
    public List<Course> findByAdminId(Integer adminId) {
        return this.entityManager.createQuery(
                        "SELECT c FROM Course c WHERE c.isDeleted = false AND c.tutor.isDeleted = false AND c.tutor.adminId = :adminId ORDER BY c.startDate DESC",
                        Course.class
                )
                .setParameter("adminId", adminId)
                .getResultList();
    }

    @Override
    public boolean isStudentEnrolled(Integer courseId, Integer studentId) {
        Long count = this.entityManager.createQuery(
                        "SELECT COUNT(c.id) FROM Course c JOIN c.students s WHERE c.id = :courseId AND s.id = :studentId",
                        Long.class
                )
                .setParameter("courseId", courseId)
                .setParameter("studentId", studentId)
                .getSingleResult();

        return count != null && count > 0;
    }

    @Override
    public long countEnrolledStudents(Integer id) {
        Long count = this.entityManager.createQuery(
                        "SELECT COUNT(s.id) FROM Course c JOIN c.students s WHERE c.id = :courseId",
                        Long.class
                )
                .setParameter("courseId", id)
                .getSingleResult();

        return (count == null) ? 0L : count;
    }

    @Override
    public boolean enrollStudent(Integer courseId, Integer studentId) {
        int inserted = this.entityManager.createNativeQuery(
                        "INSERT INTO course_student (cour_id, stud_id) VALUES (:courseId, :studentId)"
                )
                .setParameter("courseId", courseId)
                .setParameter("studentId", studentId)
                .executeUpdate();

        return inserted == 1;
    }

    @Override
    public boolean softDelete(Integer id) {
        int updated = this.entityManager.createQuery(
                        "UPDATE Course c SET c.isDeleted = true WHERE c.id = :id"
                )
                .setParameter("id", id)
                .executeUpdate();

        return updated == 1;
    }

    @Override
    public boolean hardDelete(Integer id) {
        Course course = this.entityManager.find(Course.class, id);
        if (course == null) return false;

        this.entityManager.remove(course);
        return true;
    }

    @Override
    public boolean hasAnyForums(Integer courseId) {
        Long count = this.entityManager.createQuery(
                        "SELECT COUNT(f.id) FROM Forum f WHERE f.course.id = :courseId",
                        Long.class
                )
                .setParameter("courseId", courseId)
                .getSingleResult();

        return count != null && count > 0;
    }

    @Override
    public boolean hasAnyGrades(Integer courseId) {
        Long count = this.entityManager.createQuery(
                        "SELECT COUNT(g.id) FROM Grade g WHERE g.course.id = :courseId",
                        Long.class
                )
                .setParameter("courseId", courseId)
                .getSingleResult();

        return count != null && count > 0;
    }

    @Override
    public boolean hasAnyReports(Integer courseId) {
        Long count = this.entityManager.createQuery(
                        "SELECT COUNT(r.id) FROM Report r WHERE r.course.id = :courseId",
                        Long.class
                )
                .setParameter("courseId", courseId)
                .getSingleResult();

        return count != null && count > 0;
    }

    @Override
    public int updateStatusToInProgress(LocalDate today) {
        return this.entityManager.createQuery(
                        "UPDATE Course c SET c.status = :inProgress WHERE c.isDeleted = false AND c.status = :active " +
                        "AND c.startDate <= :today"
                )
                .setParameter("inProgress", CourseStatus.IN_PROGRESS)
                .setParameter("active", CourseStatus.ACTIVE)
                .setParameter("today", today)
                .executeUpdate();
    }

    @Override
    public int updateStatusToInactive(LocalDate today) {
        return this.entityManager.createQuery(
                        "UPDATE Course c SET c.status = :inactive WHERE c.isDeleted = false AND c.status <> :inactive AND c.endDate < :today"
                )
                .setParameter("inactive", CourseStatus.INACTIVE)
                .setParameter("today", today)
                .executeUpdate();
    }
}
