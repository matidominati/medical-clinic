package com.github.matidominati.medicalclinic.mapper;

import com.github.matidominati.medicalclinic.dataFactory.TestDataFactory;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.model.entity.Institution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Stream;

import static com.github.matidominati.medicalclinic.dataFactory.TestDataFactory.*;
import static com.github.matidominati.medicalclinic.dataFactory.TestDataFactory.createDoctor;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DoctorMapperTest {
    DoctorMapper doctorMapper = Mappers.getMapper(DoctorMapper.class);

    @ParameterizedTest
    @MethodSource("doctorData")
    void MapDoctorToDoctorDto_CorrectDoctorData_DoctorDtoReturned(Doctor doctor) {
        var result = doctorMapper.doctorToDoctorDto(doctor);

        assertAll(
                () -> assertEquals(doctor.getId(), result.getId()),
                () -> assertEquals(doctor.getFirstName(), result.getFirstName()),
                () -> assertEquals(doctor.getLastName(), result.getLastName()),
                () -> assertEquals(doctor.getSpecialization(), result.getSpecialization()),
                () -> assertEquals(doctor.getInstitutions(), result.getInstitutions())
        );
    }

    public static Stream<Arguments> doctorData() {
        Institution institution1 = createInstitution(1L, "NFZ", "Warszawa", null);
        Institution institution2 = createInstitution(1L, null, null, null);
        Doctor doctor1 = createDoctor(1L, "Mariusz", "Kowalski", "chirurg",
                "123456789", List.of(institution1));
        Doctor doctor2 = createDoctor(1L, "Mariusz", "Kowalski", "chirurg",
                null, List.of(institution2));

        return Stream.of(
                Arguments.of(doctor1),
                Arguments.of(doctor2)
        );
    }
}