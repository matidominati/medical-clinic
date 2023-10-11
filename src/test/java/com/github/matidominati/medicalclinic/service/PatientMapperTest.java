package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.model.Patient;
import com.github.matidominati.medicalclinic.service.dto.PatientDto;
import com.github.matidominati.medicalclinic.service.mapper.PatientMapper;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatientMapperTest {

    @Test
    void PatientEntityGiven_mapToDto_PatientDtoReturned() {
        Patient patient = new Patient(1, "andrzej.golota@gmail.com", "andrzej1", "123456", "Andrzej",
                "Golota", "99999999", LocalDate.of(1960, 5, 10));

        PatientDto patientDto = PatientMapper.mapToDto(patient);

        assertEquals("andrzej.golota@gmail.com", patientDto.getEmail());
        assertEquals("Andrzej", patientDto.getFirstName());
        assertEquals("Golota", patientDto.getLastName());
        assertEquals("99999999", patientDto.getPhoneNumber());
        assertEquals(LocalDate.of(1960, 5, 10), patientDto.getBirthDate());
    }
}
