package com.github.matidominati.medicalclinic.model.dto.commandDto.editCommand;

import com.github.matidominati.medicalclinic.model.entity.Doctor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class EditInstitutionCommand {
    private String name;
    private String address;
    private List<Doctor> doctors;
}
