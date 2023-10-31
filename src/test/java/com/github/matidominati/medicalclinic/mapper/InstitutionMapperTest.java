package com.github.matidominati.medicalclinic.mapper;

import com.github.matidominati.medicalclinic.dataFactory.TestDataFactory;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.model.entity.Institution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.matidominati.medicalclinic.dataFactory.TestDataFactory.*;
import static com.github.matidominati.medicalclinic.mapper.DoctorMapperTest.*;
import static org.junit.jupiter.api.Assertions.*;

public class InstitutionMapperTest {
    InstitutionMapper institutionMapper = Mappers.getMapper(InstitutionMapper.class);

    @ParameterizedTest
    @MethodSource("institutionData")
    void MapInstitutionToInstitutionDto_CorrectInstitutionData_InstitutionDtoReturned(Institution institution) {
        var result = institutionMapper.institutionToInstitutionDto(institution);

        assertAll(
                () -> assertEquals(institution.getId(), result.getId()),
                () -> assertEquals(institution.getName(), result.getName()),
                () -> assertEquals(institution.getAddress(), result.getAddress()),
                () -> assertEquals(institution.getDoctors().stream()
                        .map(doctor -> doctor.getId())
                                .collect(Collectors.toList()), result.getDoctorIds())
        );

    }

    public static Stream<Arguments> institutionData() {
        Doctor doctor1 = createDoctor(1L, "Andrzej", "Golota", "onkolog",
                "123456789", null);
        Doctor doctor2 = createDoctor(2L, "Jan", "Golota", "onkolog",
                "123456789", null);
        Institution institution1 = createInstitution(1L, "NFZ", "Warszawa", List.of(doctor1,doctor2));
        Institution institution2 = createInstitution(1L, "NFZ", "Gdansk", List.of(doctor2));

        return Stream.of(
                Arguments.of(institution1),
                Arguments.of(institution2)
        );
    }
}
