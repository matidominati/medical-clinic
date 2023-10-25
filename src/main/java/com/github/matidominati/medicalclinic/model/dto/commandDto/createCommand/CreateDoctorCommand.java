package com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand;

import com.github.matidominati.medicalclinic.model.dto.InstitutionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDoctorCommand extends CreateUserCommand {
    private String specialization;
    private List<InstitutionDto> institutions;
}

