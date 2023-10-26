package com.github.matidominati.medicalclinic.mapper;

import com.github.matidominati.medicalclinic.model.entity.Patient;
import com.github.matidominati.medicalclinic.model.entity.User;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.stream.Stream;

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
        Patient patient1 = Patient.builder()
                .id(1L)
                .firstName("Jan")
                .lastName("Nowak")
                .phoneNumber("123456789")
                .idCardNo("12345")
                .birthDate(LocalDate.of(1998, 05, 10))
                .user(User.builder()
                        .email("jan@nowak.pl")
                        .username("janek123")
                        .password("123janek")
                        .id(1L)
                        .build())
                .build();
        Patient patient2 = Patient.builder()
                .id(1L)
                .firstName("Jan")
                .lastName("Nowak")
                .phoneNumber("123456789")
                .idCardNo("12345")
                .birthDate(LocalDate.of(1998, 05, 10))
                .build();
        Patient patient3 = Patient.builder()
                .id(1L)
                .firstName("Jan")
                .lastName("Nowak")
                .phoneNumber("123456789")
                .birthDate(LocalDate.of(1998, 05, 10))
                .build();

        return Stream.of(
                Arguments.of(patient1),
                Arguments.of(patient2),
                Arguments.of(patient3)
        );
    }
}