package com.tutorias.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tutorias.Repository.IStudentRepository;
import com.tutorias.Repository.ITutorRepository;
import com.tutorias.Repository.IUserRepository;
import com.tutorias.Repository.Enums.Role;
import com.tutorias.Repository.Model.Student;
import com.tutorias.Repository.Model.Tutor;
import com.tutorias.Repository.Model.User;
import com.tutorias.Service.dto.ProfileDTO;
import com.tutorias.Service.exception.NotFoundException;
import com.tutorias.Service.mapper.UserMapper;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ITutorRepository tutorRepository;

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ProfileDTO getProfile(Integer userId){

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // Tutor
        if (user.getRole() == Role.TUTOR) {
            Tutor tutor = this.tutorRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Tutor no encontrado para este usuario"));

            return this.userMapper.convertToProfileDTO(user, tutor);
        }

        // Estudiante
        if (user.getRole() == Role.STUDENT) {
            Student student = this.studentRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Estudiante no encontrado para este usuario"));

            return this.userMapper.convertToProfileDTO(user, student);
        }

        return this.userMapper.convertToProfileDTO(user);
    }
}
