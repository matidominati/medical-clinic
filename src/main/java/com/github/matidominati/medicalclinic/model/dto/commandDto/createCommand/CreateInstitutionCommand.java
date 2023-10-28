package com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand;

import com.github.matidominati.medicalclinic.model.entity.Doctor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateInstitutionCommand {
    private Long id;
    private String name;
    private String address;
    private List<Doctor> doctors;
}
