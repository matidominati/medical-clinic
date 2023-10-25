package com.github.matidominati.medicalclinic.mapper;

import com.github.matidominati.medicalclinic.model.dto.VisitDto;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.model.entity.Institution;
import com.github.matidominati.medicalclinic.model.entity.Patient;
import com.github.matidominati.medicalclinic.model.entity.Visit;
import com.github.matidominati.medicalclinic.model.enums.VisitType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VisitMapperTest {
    private VisitMapper visitMapper;
    @BeforeEach
    void setup() {
        this.visitMapper = new VisitMapperImpl();
    }
    @Test
    void MapVisitToVisitDto_CorrectVisitId_VisitDtoReturned() {
        Visit visit = Visit.builder()
                .id(1L)
                .price(BigDecimal.valueOf(150))
                .status(VisitType.CREATED)
                .startDateTime(LocalDateTime.of(2024, 10, 10, 15, 15))
                .endDateTime(LocalDateTime.of(2024, 10, 10, 15, 45))
                .institution(Institution.builder()
                        .id(1L)
                        .name("NFZ")
                        .address("Warszawa")
                        .build())
                .doctor(Doctor.builder()
                        .id(1L)
                        .firstName("Jan")
                        .lastName("Nowak")
                        .build())
                .patient(Patient.builder()
                        .id(1L)
                        .firstName("Andrzej")
                        .lastName("Golota")
                        .build())
                .build();

        VisitDto visitDto = visitMapper.visitToVisitDto(visit);

        assertEquals(visit.getInstitution().getName(), visitDto.getInstitutionName());
        assertEquals(visit.getInstitution().getAddress(), visitDto.getInstitutionAddress());
        assertEquals(visit.getDoctor().getId(), visitDto.getDoctorId());
    }
}
