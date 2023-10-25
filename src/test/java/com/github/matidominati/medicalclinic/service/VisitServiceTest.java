package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.DataNotFoundException;
import com.github.matidominati.medicalclinic.mapper.VisitMapper;
import com.github.matidominati.medicalclinic.model.dto.VisitDto;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateVisitCommand;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.model.entity.Institution;
import com.github.matidominati.medicalclinic.model.entity.User;
import com.github.matidominati.medicalclinic.model.entity.Visit;
import com.github.matidominati.medicalclinic.model.enums.VisitType;
import com.github.matidominati.medicalclinic.repository.DoctorRepository;
import com.github.matidominati.medicalclinic.repository.InstitutionRepository;
import com.github.matidominati.medicalclinic.repository.PatientRepository;
import com.github.matidominati.medicalclinic.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class VisitServiceTest {
    VisitRepository visitRepository;
    DoctorRepository doctorRepository;
    InstitutionRepository institutionRepository;
    PatientRepository patientRepository;
    VisitMapper visitMapper;
    VisitService visitService;

    @BeforeEach
    void setup() {
        this.visitRepository = Mockito.mock(VisitRepository.class);
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.institutionRepository = Mockito.mock(InstitutionRepository.class);
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.visitMapper = Mappers.getMapper(VisitMapper.class);
        this.visitService = new VisitService(visitRepository, visitMapper, doctorRepository, institutionRepository, patientRepository);
    }

    @Test
    void getAllVisits_VisitsExists_VisitsReturned() {
        List<Institution> institutions = new ArrayList<>();
        Institution institution = new Institution();
        institutions.add(institution);

        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .specialization("kardiolog")
                .phoneNumber("123-456-789")
                .institutions(institutions)
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        Visit visitA = Visit.builder()
                .id(1L)
                .date(LocalDateTime.of(2023, 12, 12, 14, 15))
                .endDateTime(LocalDateTime.of(2023, 12, 12, 14, 45))
                .price(BigDecimal.valueOf(100.00))
                .status(VisitType.CREATED)
                .doctor(doctor)
                .institution(institution)
                .build();

        Visit visitB = Visit.builder()
                .id(2L)
                .date(LocalDateTime.of(2023, 12, 12, 16, 15))
                .endDateTime(LocalDateTime.of(2023, 12, 12, 17, 15))
                .price(BigDecimal.valueOf(150.00))
                .status(VisitType.CREATED)
                .doctor(doctor)
                .institution(institution)
                .build();

        List<Visit> expectedVisits = List.of(visitA, visitB);

        when(visitRepository.findAll()).thenReturn(expectedVisits);

        VisitDto visitDtoA = visitMapper.visitToVisitDto(visitA);
        VisitDto visitDtoB = visitMapper.visitToVisitDto(visitB);

        List<VisitDto> expectedVisitsDto = List.of(visitDtoA, visitDtoB);

        List<VisitDto> result = visitService.getAllVisits();

        assertEquals(expectedVisitsDto, result);
    }

    @Test
    void getVisit_IdCorrect_VisitReturned() {
        List<Institution> institutions = new ArrayList<>();
        Institution institution = new Institution();
        institutions.add(institution);

        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .specialization("kardiolog")
                .phoneNumber("123-456-789")
                .institutions(institutions)
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        Visit visitA = Visit.builder()
                .id(1L)
                .date(LocalDateTime.of(2023, 12, 12, 14, 15))
                .endDateTime(LocalDateTime.of(2023, 12, 12, 14, 45))
                .price(BigDecimal.valueOf(100.00))
                .status(VisitType.CREATED)
                .doctor(doctor)
                .institution(institution)
                .build();

        when(visitRepository.findById(visitA.getId())).thenReturn(Optional.of(visitA));

        VisitDto visitDto = visitMapper.visitToVisitDto(visitA);
        VisitDto result = visitService.getVisitsById(visitA.getId());

        assertEquals(visitDto.getStartDateTime(), result.getStartDateTime());
        assertEquals(visitDto.getDoctorId(), result.getDoctorId());
    }

    @Test
    void getVisit_VisitIdNotFound_VisitNotReturned() {
        List<Institution> institutions = new ArrayList<>();
        Institution institution = new Institution();
        institutions.add(institution);

        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .specialization("kardiolog")
                .phoneNumber("123-456-789")
                .institutions(institutions)
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        Visit visitA = Visit.builder()
                .id(1L)
                .startDateTime(LocalDateTime.of(2023, 12, 12, 14, 15))
                .endDateTime(LocalDateTime.of(2023, 12, 12, 14, 45))
                .price(BigDecimal.valueOf(100.00))
                .status(VisitType.CREATED)
                .doctor(doctor)
                .institution(institution)
                .build();

        when(visitRepository.findById(visitA.getId())).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> visitService.getVisitsById(visitA.getId()));

        assertEquals("Visit with the provided ID does not exist in the database", exception.getMessage());
    }
    @Test
    void createVisit_DoctorNotFound_VisitNotCreated() {
        List<Institution> institutions = new ArrayList<>();
        Institution institution = Institution.builder()
                .name("NFZ")
                .id(1L)
                .build();
        institutions.add(institution);

        Doctor doctor = Doctor.builder()
                .id(1L)
                .build();

        CreateVisitCommand createVisit = CreateVisitCommand.builder()
                .price(BigDecimal.valueOf(100))
                .status(VisitType.CREATED)
                .institution(institution)
                .doctorId(doctor.getId())
                .startDateTime(LocalDateTime.of(2023, 12, 12, 14, 15))
                .endDateTime(LocalDateTime.of(2023, 12, 12, 14, 45))
                .build();

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> visitService.createVisit(createVisit, doctor.getId(), institution.getId()));
        assertEquals("Doctor with the provided ID does not exist in the database", exception.getMessage());
    }

    @Test
    void createVisit_InstitutionNotFound_VisitNotCreated() {
        List<Institution> institutions = new ArrayList<>();
        Institution institution = Institution.builder()
                .name("NFZ")
                .id(1L)
                .build();
        institutions.add(institution);

        Doctor doctor = Doctor.builder()
                .id(2L)
                .build();

        CreateVisitCommand createVisit = CreateVisitCommand.builder()
                .price(BigDecimal.valueOf(100))
                .status(VisitType.CREATED)
                .institution(institution)
                .doctorId(doctor.getId())
                .startDateTime(LocalDateTime.of(2023, 12, 12, 14, 15))
                .endDateTime(LocalDateTime.of(2023, 12, 12, 14, 45))
                .build();

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(institutionRepository.findById(institution.getId())).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> visitService.createVisit(createVisit, doctor.getId(), institution.getId()));
        assertEquals("Institution with the provided ID does not exist in the database", exception.getMessage());
    }
}