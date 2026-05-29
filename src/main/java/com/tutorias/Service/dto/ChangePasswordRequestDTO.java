package com.tutorias.Service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDTO {

    private String email;

    private String password;

    private String token;
}
