package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.DataNotFoundException;
import com.github.matidominati.medicalclinic.mapper.InstitutionMapper;
import com.github.matidominati.medicalclinic.model.dto.InstitutionDto;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateInstitutionCommand;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.model.entity.Institution;
import com.github.matidominati.medicalclinic.repository.InstitutionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.matidominati.medicalclinic.dataFactory.TestDataFactory.createDoctor;
import static com.github.matidominati.medicalclinic.dataFactory.TestDataFactory.createInstitution;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class InstitutionServiceTest {
    InstitutionRepository institutionRepository;
    InstitutionMapper institutionMapper;
    InstitutionService institutionService;

    @BeforeEach
    void setup() {
        this.institutionRepository = Mockito.mock(InstitutionRepository.class);
        this.institutionMapper = Mappers.getMapper(InstitutionMapper.class);
        this.institutionService = new InstitutionService(institutionRepository, institutionMapper, null);
    }

    @Test
    void getAllInstitutions_InstitutionsExist_InstitutionsReturned() {
        Institution institution1 = createInstitution(1L, "NFZ", "Warszawa", null);
        Institution institution2 = createInstitution(2L, "NFZ", "Gdansk", null);
        List<Institution> institutions = List.of(institution1, institution2);

        when(institutionRepository.findAll()).thenReturn(institutions);

        InstitutionDto institutionDto1 = institutionMapper.institutionToInstitutionDto(institution1);
        InstitutionDto institutionDto2 = institutionMapper.institutionToInstitutionDto(institution2);
        List<InstitutionDto> institutionsDto = List.of(institutionDto1, institutionDto2);

        List<InstitutionDto> result = institutionService.getAllInstitutions();

        assertEquals(institutionsDto, result);
    }

    @Test
    void getInstitutionById_IdCorrect_InstitutionReturned() {
        Institution institution = createInstitution(1L, "NFZ", "Warszawa", null);

        when(institutionRepository.findById(institution.getId())).thenReturn(Optional.of(institution));

        InstitutionDto institutionDto = institutionMapper.institutionToInstitutionDto(institution);
        InstitutionDto result = institutionService.getInstitutionById(institution.getId());

        assertEquals(institutionDto.getId(), result.getId());
        assertEquals(institutionDto.getName(), result.getName());
        assertEquals(institutionDto.getAddress(), result.getAddress());
    }

    @Test
    void getInstitutionById_IdNotFound_InstitutionReturned() {
        Institution institution = createInstitution(1L, "NFZ", "Warszawa", null);

        when(institutionRepository.findById(institution.getId())).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> institutionService.getInstitutionById(institution.getId()));

        assertEquals("Institution with the provided ID does not exist in the database", exception.getMessage());
    }

    @Test
    void addInstitution_InstitutionDataCorrect_InstitutionReturned() {
        Doctor doctor1 = createDoctor(1L, "Andrzej", "Golota", "chirurg",
                "123456789", null);
        Doctor doctor2 = createDoctor(1L, "Andrzej", "Golota", "chirurg",
                "123456789", null);
        CreateInstitutionCommand createInstitution = CreateInstitutionCommand.builder()
                .id(1L)
                .name("NFZ")
                .address("Warszawa")
                .doctors(List.of(doctor1, doctor2))
                .build();

        when(institutionRepository.findById(createInstitution.getId())).thenReturn(Optional.empty());

        Institution createdInstitution = Institution.createInstitution(createInstitution);
        InstitutionDto result = institutionService.addInstitution(createInstitution);

        assertEquals(createdInstitution.getId(), result.getId());
        assertEquals(createdInstitution.getName(), result.getName());
        assertEquals(createdInstitution.getAddress(), result.getAddress());
        assertEquals(createdInstitution.getDoctors().stream()
                        .map(Doctor::getId)
                        .collect(Collectors.toList()),
                result.getDoctorIds());
    }

    @Test
    void addInstitution_InstitutionDataIncorrect_InstitutionNotReturned() {
        Doctor doctor = createDoctor(1L, "Andrzej", "Golota", "chirurg",
                "123456789", null);
        CreateInstitutionCommand createInstitution = CreateInstitutionCommand.builder()
                .id(1L)
                .name("NFZ")
                .address("Warszawa")
                .doctors(List.of(doctor))
                .build();

        when(institutionRepository.findById(createInstitution.getId())).thenReturn(Optional.empty());

        Institution createdInstitution = Institution.createInstitution(createInstitution);
        InstitutionDto result = institutionService.addInstitution(createInstitution);

        assertEquals(createdInstitution.getId(), result.getId());
        assertEquals(createdInstitution.getName(), result.getName());
        assertEquals(createdInstitution.getAddress(), result.getAddress());
        assertEquals(createdInstitution.getDoctors().stream()
                        .map(doc -> doctor.getId())
                        .collect(Collectors.toList()),
                result.getDoctorIds());
    }
}
