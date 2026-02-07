package com.tutorias.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tutorias.Repository.ICourseRepository;
import com.tutorias.Repository.IForumCommentRepository;
import com.tutorias.Repository.IForumRepository;
import com.tutorias.Repository.IUserRepository;
import com.tutorias.Repository.Enums.Role;
import com.tutorias.Repository.Model.Course;
import com.tutorias.Repository.Model.Forum;
import com.tutorias.Repository.Model.ForumComment;
import com.tutorias.Repository.Model.User;
import com.tutorias.Service.dto.ForumCommentDTO;
import com.tutorias.Service.dto.ForumDTO;
import com.tutorias.Service.exception.NotFoundException;
import com.tutorias.Service.mapper.InteractionMapper;

@Service
public class ForumServiceImpl implements IForumService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired 
    private ICourseRepository courseRepository;

    @Autowired 
    private IForumRepository forumRepository;

    @Autowired 
    private IForumCommentRepository forumCommentRepository;

    @Autowired 
    private InteractionMapper interactionMapper;

    // Foro
    @Override
    public ForumDTO createForum(Integer userId, Integer courseId, ForumDTO dto) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Curso no encontrado"));

        Forum forum = Forum.builder()
                .title(dto.getTitle())
                .createdAt(LocalDateTime.now())
                .editedAt(null)
                .isDeleted(false)
                .course(course)
                .build();

        this.forumRepository.save(forum);

        return this.interactionMapper.convertToForumDTO(forum);
    }

    @Override
    public List<ForumDTO> listForumsByCourse(Integer userId, Integer courseId) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Curso no encontrado"));

        return this.forumRepository.findByCourseIdNotDeleted(course.getId()).stream()
                .map(this.interactionMapper::convertToForumDTO)
                .toList();
    }

    @Override
    public ForumDTO updateForumTitle(Integer userId, Integer forumId, String newTitle) {
        Forum forum = this.forumRepository.findById(forumId)
                .orElseThrow(() -> new NotFoundException("Foro no encontrado"));

        forum.setTitle(newTitle);
        forum.setEditedAt(LocalDateTime.now());

        Forum updated = this.forumRepository.update(forum);

        return this.interactionMapper.convertToForumDTO(updated);
    }

    @Override
    public boolean deleteForum(Integer userId, Integer forumId) {
        return this.forumRepository.softDelete(forumId);
    }

    // Comentarios
    @Override
    public List<ForumCommentDTO> listComments(Integer userId, Integer forumId) {
        Forum forum = this.forumRepository.findById(forumId)
                .orElseThrow(() -> new NotFoundException("Foro no encontrado"));

        return this.forumCommentRepository.findByForumId(forum.getId()).stream()
                .map(this.interactionMapper::convertToForumCommentDTO)
                .toList();
    }

    @Override
    public ForumCommentDTO addComment(Integer userId, Integer forumId, ForumCommentDTO dto) {

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        Forum forum = this.forumRepository.findById(forumId)
                .orElseThrow(() -> new NotFoundException("Foro no encontrado"));

        ForumComment comment = ForumComment.builder()
                .comment(dto.getComment())
                .createdAt(LocalDateTime.now())
                .editedAt(null)
                .isDeleted(false)
                .forum(forum)
                .user(user)
                .build();

        this.forumCommentRepository.save(comment);

        return this.interactionMapper.convertToForumCommentDTO(comment);
    }

    @Override
    public boolean deleteComment(Integer userId, Integer commentId) {

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        ForumComment comment = this.forumCommentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comentario no encontrado"));

        boolean isAuthor = (comment.getUser() != null && comment.getUser().getId().equals(userId));
        boolean isTutor = (user.getRole() == Role.TUTOR);

        if (!isAuthor && !isTutor) {
            throw new RuntimeException("No autorizado para eliminar este comentario");
        }

        return this.forumCommentRepository.softDelete(commentId);
    }
    
}
