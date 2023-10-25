package com.github.matidominati.medicalclinic.model.dto.commandDto.editCommand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookVisitCommand {
    private Long patientId;
    private Long visitId;
}
