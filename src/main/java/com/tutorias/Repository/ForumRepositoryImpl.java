package com.tutorias.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import com.tutorias.Repository.Model.Forum;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ForumRepositoryImpl implements IForumRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Forum> findById(Integer id) {
        return Optional.ofNullable(this.entityManager.find(Forum.class, id));
    }

    @Override
    public List<Forum> findByCourseId(Integer courseId) {
        return this.entityManager.createQuery(
                        "SELECT f FROM Forum f WHERE f.course.id = :courseId ORDER BY f.createdAt DESC",
                        Forum.class
                )
                .setParameter("courseId", courseId)
                .getResultList();
    }

    @Override
    public List<Forum> findByCourseIdNotDeleted(Integer courseId) {
        return this.entityManager.createQuery(
                        "SELECT f FROM Forum f WHERE f.isDeleted = false AND f.course.id = :courseId ORDER BY f.createdAt DESC",
                        Forum.class
                )
                .setParameter("courseId", courseId)
                .getResultList();
    }

    @Override
    public Forum save(Forum forum) {
        this.entityManager.persist(forum);
        return forum;
    }

    @Override
    public Forum update(Forum forum) {
        return this.entityManager.merge(forum);
    }

    @Override
    public boolean softDelete(Integer id) {
        int updated = this.entityManager.createQuery(
                        "UPDATE Forum f SET f.isDeleted = true WHERE f.id = :id"
                )
                .setParameter("id", id)
                .executeUpdate();

        return updated == 1;
    }
}
