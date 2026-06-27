package com.tutorias.Repository;

import java.util.List;
import java.util.Optional;

import com.tutorias.Repository.Model.User;

public interface IUserRepository {

    public Optional<User> findById(Integer id);

    public Optional<User> findByEmail(String email);

    public List<User> findAllAdmins();

    public boolean existsByEmail(String email);

    public boolean existsByIdCard(String idCard);

    public User save(User user);

    public User update(User user);

    public boolean updatePassword(String email, String password);

    public boolean softDelete(Integer id);

    public boolean hardDelete(Integer id);

    // Busca por cédula aunque el usuario esté desactivado (soft-deleted). Para reactivar.
    public Optional<User> findByIdCard(String idCard);

    // Reactiva (restore) un usuario: isDeleted = false.
    public boolean restore(Integer id);
}
