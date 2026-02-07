package com.tutorias.Repository;

import com.tutorias.Repository.Model.Forum;
import java.util.List;
import java.util.Optional;

public interface IForumRepository {

    public Optional<Forum> findById(Integer id);

    public List<Forum> findByCourseId(Integer courseId);

    public List<Forum> findByCourseIdNotDeleted(Integer courseId);

    public Forum save(Forum forum);

    public Forum update(Forum forum);

    public boolean softDelete(Integer id);
}
