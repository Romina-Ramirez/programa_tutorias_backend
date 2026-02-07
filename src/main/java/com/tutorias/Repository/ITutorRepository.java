package com.tutorias.Repository;

import java.util.List;
import java.util.Optional;

import com.tutorias.Repository.Model.Tutor;

public interface ITutorRepository {

    public Optional<Tutor> findById(Integer id);

    public Optional<Tutor> findByUserId(Integer userId);

    public boolean existsById(Integer id);

    public List<Tutor> findByAdminId(Integer adminId);

    public Tutor save(Tutor tutor);

    public Tutor update(Tutor tutor);

    public boolean activate(Integer id);

    public boolean softDelete(Integer id);
}
