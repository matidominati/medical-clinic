package com.github.matidominati.medicalclinic.model.dto;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientDto {

    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthDate;
}
