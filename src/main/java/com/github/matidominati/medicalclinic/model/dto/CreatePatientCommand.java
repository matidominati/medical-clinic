package com.github.matidominati.medicalclinic.model.dto;

import lombok.*;

import java.time.LocalDate;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class CreatePatientCommand extends CreateUserCommand{
    private String idCardNo;
    private LocalDate birthDate;

}


