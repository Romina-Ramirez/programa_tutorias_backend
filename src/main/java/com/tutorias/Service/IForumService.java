package com.tutorias.Service;

import java.util.List;

import com.tutorias.Service.dto.ForumCommentDTO;
import com.tutorias.Service.dto.ForumDTO;

public interface IForumService {

    // Foro
    public ForumDTO createForum(Integer userId, Integer courseId, ForumDTO dto);

    public List<ForumDTO> listForumsByCourse(Integer userId, Integer courseId);

    public ForumDTO updateForumTitle(Integer userId, Integer forumId, String newTitle);

    public boolean deleteForum(Integer userId, Integer forumId);

    // Comentario
    public List<ForumCommentDTO> listComments(Integer userId, Integer forumId);

    public ForumCommentDTO addComment(Integer userId, Integer forumId, ForumCommentDTO dto);

    public boolean deleteComment(Integer userId, Integer commentId);
}
