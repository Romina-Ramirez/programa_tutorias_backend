package com.tutorias.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorias.Service.IUserService;
import com.tutorias.Service.dto.ProfileDTO;

@RestController
@CrossOrigin
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/{userId}/profile")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getProfile(userId));
    }
    
}