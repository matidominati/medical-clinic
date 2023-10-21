package com.github.matidominati.medicalclinic.model.dto;

import com.github.matidominati.medicalclinic.model.entity.Institution;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class CreateDoctorCommand extends CreateUserCommand {
    private String specialization;
    private List<Institution> institutions;
    private String institutionName;
    private String institutionAddress;
}

