package com.tutorias.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import com.tutorias.Repository.Model.GeneralReport;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class GeneralReportRepositoryImpl implements IGeneralReportRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<GeneralReport> findById(Integer id) {
        return Optional.ofNullable(this.entityManager.find(GeneralReport.class, id));
    }

    @Override
    public Optional<GeneralReport> findByTutorId(Integer tutorId) {
        List<GeneralReport> generalReports = this.entityManager.createQuery(
                        "SELECT gr FROM GeneralReport gr WHERE gr.isDeleted = false AND gr.tutor.id = :tutorId",
                        GeneralReport.class
                )
                .setParameter("tutorId", tutorId)
                .setMaxResults(1)
                .getResultList();

        return generalReports.isEmpty() ? Optional.empty() : Optional.of(generalReports.get(0));
    }

    @Override
    public GeneralReport save(GeneralReport generalReport) {              
        this.entityManager.persist(generalReport);
        return generalReport;
    }

    @Override
    public GeneralReport update(GeneralReport generalReport) {
        return this.entityManager.merge(generalReport);
    }

    @Override
    public boolean softDelete(Integer id) {
        int updated = this.entityManager.createQuery(
                        "UPDATE GeneralReport gr SET gr.isDeleted = true WHERE gr.id = :id"
                )
                .setParameter("id", id)
                .executeUpdate();

        return updated == 1;
    }
}
