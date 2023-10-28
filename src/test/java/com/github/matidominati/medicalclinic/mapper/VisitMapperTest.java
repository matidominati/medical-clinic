package com.github.matidominati.medicalclinic.mapper;

import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.model.entity.Institution;
import com.github.matidominati.medicalclinic.model.entity.Patient;
import com.github.matidominati.medicalclinic.model.entity.Visit;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Stream;

import static com.github.matidominati.medicalclinic.model.entity.Doctor.createDoctorWithParameters;
import static com.github.matidominati.medicalclinic.model.entity.Patient.createPatientWithParameters;
import static com.github.matidominati.medicalclinic.model.entity.Visit.createVisitWithParameters;
import static com.github.matidominati.medicalclinic.model.enums.VisitType.CREATED;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VisitMapperTest {
    VisitMapper visitMapper = Mappers.getMapper(VisitMapper.class);

    @ParameterizedTest
    @MethodSource("correctVisitData")
    void MapVisitToVisitDto_CorrectVisitData_VisitDtoReturned(Visit visit) {
        var result = visitMapper.visitToVisitDto(visit);

        assertAll(
                () -> assertEquals(visit.getId(), result.getId()),
                () -> assertEquals(visit.getInstitution().getName(), result.getInstitutionName()),
                () -> assertEquals(visit.getInstitution().getAddress(), result.getInstitutionAddress()),
                () -> assertEquals(visit.getDoctor().getId(), result.getDoctorId()),
                () -> assertEquals(visit.getPrice(), result.getPrice()),
                () -> assertEquals(visit.getStartDateTime(), result.getStartDateTime()),
                () -> assertEquals(visit.getEndDateTime(), result.getEndDateTime()),
                () -> assertEquals(visit.getStatus(), result.getStatus())
        );
    }

    public static Stream<Arguments> correctVisitData() {
        Doctor doctor = createDoctorWithParameters(1L, "Mariusz", "Kowalski", "chirurg",
                "123456789", new ArrayList<>());
        Doctor doctor2 = createDoctorWithParameters(1L, null, null, null, null, null);
        Patient patient = createPatientWithParameters(1L, "Andrzej", "Kielbasa", "123456789",
                "12345", LocalDate.of(1998, 10, 10));
        Institution institution = Institution.createInstitutionWithParameters(1L, "NFZ", "Warszawa", null);
        Institution institution2 = Institution.createInstitutionWithParameters(null, "NFZ", "Warszawa", null);
        Visit visit1 = createVisitWithParameters(1L, CREATED, BigDecimal.valueOf(150),
                LocalDateTime.of(2024, 10, 10, 15, 15),
                LocalDateTime.of(2024, 10, 10, 16, 0), doctor, patient, institution);
        Visit visit2 = createVisitWithParameters(1L, CREATED, BigDecimal.valueOf(150),
                LocalDateTime.of(2024, 10, 10, 15, 15),
                LocalDateTime.of(2024, 10, 10, 16, 0), doctor, patient, institution2);
        Visit visit3 = createVisitWithParameters(1L, CREATED, BigDecimal.valueOf(150),
                LocalDateTime.of(2024, 10, 10, 15, 15),
                LocalDateTime.of(2024, 10, 10, 16, 0), doctor, null, institution);
        Visit visit4 = createVisitWithParameters(1L, CREATED, BigDecimal.valueOf(150),
                LocalDateTime.of(2024, 10, 10, 15, 15),
                LocalDateTime.of(2024, 10, 10, 16, 0), doctor2, null, institution2);

        return Stream.of(
                Arguments.of(visit1),
                Arguments.of(visit2),
                Arguments.of(visit3),
                Arguments.of(visit4)
        );
    }
}
