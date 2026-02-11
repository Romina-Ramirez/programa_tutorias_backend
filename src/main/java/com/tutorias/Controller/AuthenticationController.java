package com.tutorias.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tutorias.Service.IAuthenticationService;
import com.tutorias.Service.dto.LoginRequestDTO;
import com.tutorias.Service.dto.LoginResponseDTO;
import com.tutorias.Service.dto.RegisterStudentDTO;

@RestController
@CrossOrigin
@RequestMapping(path = "/authentication")
public class AuthenticationController {

    @Autowired
    private IAuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authenticationService.login(dto.getEmail(), dto.getPassword()));
    }

    @PostMapping("/register-student")
    public ResponseEntity<Boolean> registerStudent(@RequestBody RegisterStudentDTO dto) {
        return ResponseEntity.ok(authenticationService.registerStudent(dto));
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<Boolean> requestPasswordReset(@RequestParam String email) {
        return ResponseEntity.ok(authenticationService.requestChangePassword(email));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authenticationService.changePassword(dto.getEmail(), dto.getPassword()));
    }
    
}