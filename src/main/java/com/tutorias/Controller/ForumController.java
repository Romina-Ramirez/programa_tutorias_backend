package com.tutorias.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorias.Config.SecurityAccessHelper;
import com.tutorias.Service.IForumService;
import com.tutorias.Service.dto.ForumCommentDTO;
import com.tutorias.Service.dto.ForumDTO;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@CrossOrigin
@RequestMapping(path = "/forums")
public class ForumController {

    @Autowired
    private IForumService forumService;

    @Autowired
    private SecurityAccessHelper securityAccessHelper;

    // Foros por curso
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ForumDTO>> listForumsByCourse(@RequestParam Integer userId, @PathVariable Integer courseId) {
        securityAccessHelper.requireSameUser(userId);
        return ResponseEntity.ok(forumService.listForumsByCourse(userId, courseId));
    }

    @PostMapping("/course/{courseId}")
    public ResponseEntity<ForumDTO> createForum(@RequestParam Integer userId, @PathVariable Integer courseId, @RequestBody ForumDTO dto) {
        securityAccessHelper.requireSameUser(userId);
        return ResponseEntity.ok(forumService.createForum(userId, courseId, dto));
    }

    @PutMapping("/{forumId}")
    public ResponseEntity<ForumDTO> updateForumTitle(@RequestParam Integer userId, @PathVariable Integer forumId, @RequestBody String title) {
        securityAccessHelper.requireSameUser(userId);
        return ResponseEntity.ok(forumService.updateForumTitle(userId, forumId, title));
    }

    @DeleteMapping("/{forumId}")
    public ResponseEntity<Boolean> deleteForum(@RequestParam Integer userId, @PathVariable Integer forumId) {
        securityAccessHelper.requireSameUser(userId);
        return ResponseEntity.ok(forumService.deleteForum(userId, forumId));
    }

    // Comentarios
    @GetMapping("/{forumId}/comments")
    public ResponseEntity<List<ForumCommentDTO>> listComments(@RequestParam Integer userId, @PathVariable Integer forumId) {
        securityAccessHelper.requireSameUser(userId);
        return ResponseEntity.ok(forumService.listComments(userId, forumId));
    }

    @PostMapping("/{forumId}/comments")
    public ResponseEntity<ForumCommentDTO> addComment(@RequestParam Integer userId, @PathVariable Integer forumId, @RequestBody ForumCommentDTO dto) {
        securityAccessHelper.requireSameUser(userId);
        return ResponseEntity.ok(forumService.addComment(userId, forumId, dto));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Boolean> deleteComment(@RequestParam Integer userId, @PathVariable Integer commentId) {
        securityAccessHelper.requireSameUser(userId);
        return ResponseEntity.ok(forumService.deleteComment(userId, commentId));
    }
    
}
