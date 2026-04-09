package com.tutorias.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tutorias.Repository.IStudentRepository;
import com.tutorias.Repository.IUserRepository;
import com.tutorias.Repository.Enums.Role;
import com.tutorias.Repository.Model.User;
import com.tutorias.Service.dto.ProfileDTO;
import com.tutorias.Service.exception.NotFoundException;
import com.tutorias.Service.mapper.UserMapper;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ProfileDTO getProfile(Integer userId){

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (user.getRole() == Role.STUDENT) {
            this.studentRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Estudiante no encontrado para este usuario"));

            return this.userMapper.convertToProfileDTO(user);
        }

        return this.userMapper.convertToProfileDTO(user);
    }
}
