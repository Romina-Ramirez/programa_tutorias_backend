package com.tutorias.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import com.tutorias.Repository.Model.ForumComment;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ForumCommentRepositoryImpl implements IForumCommentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ForumComment> findById(Integer id) {
        return Optional.ofNullable(this.entityManager.find(ForumComment.class, id));
    }

    @Override
    public List<ForumComment> findByForumId(Integer forumId) {
        return this.entityManager.createQuery(
                        "SELECT c FROM ForumComment c WHERE c.isDeleted = false AND c.forum.id = :forumId ORDER BY c.createdAt ASC",
                        ForumComment.class
                )
                .setParameter("forumId", forumId)
                .getResultList();
    }

    @Override
    public ForumComment save(ForumComment comment) {
        this.entityManager.persist(comment);
        return comment;
    }

    @Override
    public ForumComment update(ForumComment comment) {
        return this.entityManager.merge(comment);
    }

    @Override
    public boolean softDelete(Integer id) {
        int updated = this.entityManager.createQuery(
                        "UPDATE ForumComment c SET c.isDeleted = true WHERE c.id = :id"
                )
                .setParameter("id", id)
                .executeUpdate();

        return updated == 1;
    }
}
