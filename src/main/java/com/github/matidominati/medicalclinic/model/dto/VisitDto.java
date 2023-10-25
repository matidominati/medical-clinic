package com.github.matidominati.medicalclinic.model.dto;

import com.github.matidominati.medicalclinic.model.enums.VisitType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class VisitDto {
    private BigDecimal price;
    private String institutionName;
    private String institutionAddress;
    private Long doctorId;
    private LocalDateTime date;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private VisitType status;
}
