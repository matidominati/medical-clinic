package com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand;

import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateUserCommand;
import lombok.*;

import java.time.LocalDate;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePatientCommand extends CreateUserCommand {
    private String idCardNo;
    private LocalDate birthDate;

}


