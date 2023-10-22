package com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand;

//import com.github.matidominati.medicalclinic.model.enums.VisitType;

import com.github.matidominati.medicalclinic.model.entity.Doctor;
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
    private Doctor doctor;
    private Institution institution;
    private LocalDateTime date;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
