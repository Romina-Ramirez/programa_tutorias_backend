package com.tutorias.Repository;

import java.util.Optional;

import com.tutorias.Repository.Model.User;

public interface IUserRepository {

    public Optional<User> findById(Integer id);

    public Optional<User> findByEmail(String email);

    public boolean existsByEmail(String email);

    public User save(User user);

    public User update(User user);

    public boolean updatePassword(String email, String password);

    public boolean softDelete(Integer id);

    public boolean hardDelete(Integer id);
}
