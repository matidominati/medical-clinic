package com.github.matidominati.medicalclinic.model.dto.commandDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EditPatientCommand {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;

}
