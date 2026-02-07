package com.tutorias.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.tutorias.Repository.Model.User;

@Repository
@Transactional
public class UserRepositoryImpl implements IUserRepository {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(this.entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> users = this.entityManager.createQuery(
                        "SELECT u FROM User u WHERE u.email = :email",
                        User.class
                )
                .setParameter("email", email)
                .setMaxResults(1)
                .getResultList();

        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public boolean existsByEmail(String email) {
        Long count = this.entityManager.createQuery(
                        "SELECT COUNT(u.id) FROM User u WHERE u.email = :email",
                        Long.class
                )
                .setParameter("email", email)
                .getSingleResult();

        return count != null && count > 0;
    }

    @Override
    public User save(User user) {
        this.entityManager.persist(user);
        return user;
    }

    @Override
    public User update(User user) {
        return this.entityManager.merge(user);
    }

    @Override
    public boolean updatePassword(String email, String password) {
        int updated = this.entityManager.createQuery(
                        "UPDATE User u SET u.password = :password WHERE u.email = :email"
                )
                .setParameter("password", password)
                .setParameter("email", email)
                .executeUpdate();

        return updated == 1;
    }

    @Override
    public boolean softDelete(Integer id) {
        int updated = this.entityManager.createQuery(
                        "UPDATE User u SET u.isDeleted = true WHERE u.id = :id"
                )
                .setParameter("id", id)
                .executeUpdate();

        return updated == 1;
    }

    @Override
    public boolean hardDelete(Integer id) {
        User user = this.entityManager.find(User.class, id);
        if (user == null) return false;

        this.entityManager.remove(user);
        return true;
    }
}
