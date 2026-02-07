package com.tutorias.Repository;

import com.tutorias.Repository.Model.ForumComment;
import java.util.List;
import java.util.Optional;

public interface IForumCommentRepository {

    public Optional<ForumComment> findById(Integer id);

    public List<ForumComment> findByForumId(Integer forumId);

    public ForumComment save(ForumComment comment);

    public ForumComment update(ForumComment comment);

    public boolean softDelete(Integer id);
}
