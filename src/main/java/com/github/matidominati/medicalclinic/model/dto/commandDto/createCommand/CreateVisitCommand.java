package com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand;

import com.github.matidominati.medicalclinic.model.dto.InstitutionDto;
import com.github.matidominati.medicalclinic.model.entity.Institution;
import com.github.matidominati.medicalclinic.model.enums.VisitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateVisitCommand {
    private VisitType status;
    private BigDecimal price;
    private Long doctorId;
    private Institution institution;                              ;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
