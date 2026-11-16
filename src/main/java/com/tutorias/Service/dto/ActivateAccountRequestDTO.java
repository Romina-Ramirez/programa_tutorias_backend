package com.tutorias.Service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivateAccountRequestDTO {

    private String token;

    private String password;
}
