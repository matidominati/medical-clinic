package com.github.matidominati.medicalclinic.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PatientDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthDate;
}
