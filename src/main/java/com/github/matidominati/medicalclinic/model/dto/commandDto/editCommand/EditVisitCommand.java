package com.github.matidominati.medicalclinic.model.dto.commandDto.editCommand;

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
public class EditVisitCommand {
    private BigDecimal price;
    private LocalDateTime date;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
