package com.github.matidominati.medicalclinic.model.dto.commandDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserCommand {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}

