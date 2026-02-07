package com.tutorias.Service;

import com.tutorias.Service.dto.LoginResponseDTO;
import com.tutorias.Service.dto.RegisterStudentDTO;

public interface IAuthenticationService {

    public LoginResponseDTO login(String email, String password);

    public boolean registerStudent(RegisterStudentDTO dto);

    public boolean requestChangePassword(String email);

    public boolean changePassword(String email, String newPassword);
    
}
