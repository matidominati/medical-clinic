package com.github.matidominati.medicalclinic.model.dto.commandDto;

import com.github.matidominati.medicalclinic.model.entity.Institution;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class EditDoctorCommand {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private List<Institution> institutions;
    private String specialization;
}

