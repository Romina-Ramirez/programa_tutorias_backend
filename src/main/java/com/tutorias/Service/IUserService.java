package com.tutorias.Service;

import com.tutorias.Service.dto.ProfileDTO;

public interface IUserService {

    public ProfileDTO getProfile(Integer userId);
    
}
