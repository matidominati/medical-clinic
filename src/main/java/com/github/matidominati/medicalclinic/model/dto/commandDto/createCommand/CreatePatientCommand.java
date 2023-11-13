package com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class CreatePatientCommand extends CreateUserCommand {
    private String idCardNo;
    private LocalDate birthDate;
}


