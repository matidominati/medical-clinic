package com.github.matidominati.medicalclinic.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class DoctorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;
    private List<InstitutionDto> institutions;
}
