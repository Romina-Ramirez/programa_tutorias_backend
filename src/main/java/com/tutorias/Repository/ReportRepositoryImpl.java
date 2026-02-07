package com.tutorias.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import com.tutorias.Repository.Model.Report;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ReportRepositoryImpl implements IReportRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Report> findById(Integer id) {
        return Optional.ofNullable(this.entityManager.find(Report.class, id));
    }

    @Override
    public Report save(Report report) {
        this.entityManager.persist(report);
        return report;
    }

    @Override
    public Report update(Report report) {
        return this.entityManager.merge(report);
    }

    @Override
    public boolean hardDelete(Integer id) {
        Report report = this.entityManager.find(Report.class, id);
        if (report == null) return false;

        this.entityManager.remove(report);
        return true;
    }

    @Override
    public List<Report> findByCourseId(Integer courseId) {
        return this.entityManager.createQuery(
                        "SELECT r FROM Report r WHERE r.course.id = :courseId ORDER BY r.createdAt DESC",
                        Report.class
                )
                .setParameter("courseId", courseId)
                .getResultList();
    }

    @Override
    public List<Report> findByGeneralReportId(Integer generalReportId) {
        return this.entityManager.createQuery(
                        "SELECT r FROM Report r WHERE r.generalReport.id = :generalReportId ORDER BY r.createdAt DESC",
                        Report.class
                )
                .setParameter("generalReportId", generalReportId)
                .getResultList();
    }
}
