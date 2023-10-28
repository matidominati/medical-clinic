package com.github.matidominati.medicalclinic.mapper;

import com.github.matidominati.medicalclinic.model.entity.Patient;
import com.github.matidominati.medicalclinic.model.entity.User;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.stream.Stream;

import static com.github.matidominati.medicalclinic.model.entity.Patient.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatientMapperTest {
    PatientMapper patientMapper = Mappers.getMapper(PatientMapper.class);

    @ParameterizedTest
    @MethodSource("correctPatientData")
    void MapPatientToPatientDto_CorrectPatientData_PatientDtoReturned(Patient patient) {
        var result = patientMapper.patientToPatientDto(patient);

        assertAll(
                () -> assertEquals(patient.getId(), result.getId()),
                () -> assertEquals(patient.getFirstName(), result.getFirstName()),
                () -> assertEquals(patient.getLastName(), result.getLastName()),
                () -> assertEquals(patient.getPhoneNumber(), result.getPhoneNumber()),
                () -> assertEquals(patient.getBirthDate(), result.getBirthDate())
        );
    }

    public static Stream<Arguments> correctPatientData() {
        Patient patient1 = createPatientWithParameters(1L, "Jan", "Nowak", "123456789",
                "12345", LocalDate.of(1990, 10, 10));
        Patient patient2 = createPatientWithParameters(1L, "Jan", "Nowak", "123456789",
                null, LocalDate.of(1990, 10, 10));

        return Stream.of(
                Arguments.of(patient1),
                Arguments.of(patient2)
        );
    }
}