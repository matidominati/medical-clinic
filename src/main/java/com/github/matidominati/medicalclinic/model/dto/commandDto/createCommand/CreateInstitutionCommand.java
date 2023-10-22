package com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand;

import com.github.matidominati.medicalclinic.model.entity.Doctor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInstitutionCommand {
    private String name;
    private String address;
    private List<Doctor> doctors;
}
