package com.tutorias.Service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Son los datos del tutor 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TutorDTO {

    private String name;
    private String lastName;
    private String email;
    private String career;
    private String phone;
    private String availableSchedule;
    private boolean isActive;
    
}
