package com.tutorias.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.tutorias.Repository.Model.Tutor;

@Repository
@Transactional
public class TutorRepositoryImpl implements ITutorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Tutor> findById(Integer id) {
        return Optional.ofNullable(this.entityManager.find(Tutor.class, id));
    }

    @Override
    public Optional<Tutor> findByUserId(Integer userId) {
        List<Tutor> tutors = entityManager.createQuery(
                        "SELECT t FROM Tutor t WHERE t.user.id = :userId",
                        Tutor.class
                )
                .setParameter("userId", userId)
                .setMaxResults(1)
                .getResultList();

        return tutors.isEmpty() ? Optional.empty() : Optional.of(tutors.get(0));
    }

    @Override
    public boolean existsById(Integer id) {
        Long count = this.entityManager.createQuery(
                        "SELECT COUNT(t.id) FROM Tutor t WHERE t.id = :id",
                        Long.class
                )
                .setParameter("id", id)
                .getSingleResult();

        return count != null && count > 0;
    }

    @Override
    public List<Tutor> findByAdminId(Integer adminId) {
        return this.entityManager.createQuery(
                        "SELECT t FROM Tutor t WHERE t.isDeleted = false AND t.adminId = :adminId ORDER BY t.id DESC",
                        Tutor.class
                )
                .setParameter("adminId", adminId)
                .getResultList();
    }

    @Override
    public Tutor save(Tutor tutor) {
        this.entityManager.persist(tutor);
        return tutor;
    }

    @Override
    public Tutor update(Tutor tutor) {
        return this.entityManager.merge(tutor);
    }

    @Override
    public boolean activate(Integer id) {
        int updated = this.entityManager.createQuery(
                    "UPDATE Tutor t SET t.isActive = true WHERE t.id = :id"
            )
            .setParameter("id", id)
            .executeUpdate();

        return updated == 1;
    }

    @Override
    public boolean softDelete(Integer id) {
        int updated = this.entityManager.createQuery(
                        "UPDATE Tutor t SET t.isDeleted = true WHERE t.id = :id"
                )
                .setParameter("id", id)
                .executeUpdate();

        return updated == 1;
    }
}
