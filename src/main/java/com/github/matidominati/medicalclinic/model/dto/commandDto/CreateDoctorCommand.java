package com.github.matidominati.medicalclinic.model.dto.commandDto;

import com.github.matidominati.medicalclinic.model.dto.InstitutionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class CreateDoctorCommand extends CreateUserCommand {
    private String specialization;
    private List<InstitutionDto> institutions;
}

