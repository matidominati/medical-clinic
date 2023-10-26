package com.github.matidominati.medicalclinic.mapper;

import com.github.matidominati.medicalclinic.exception.DataNotFoundException;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.model.entity.Institution;
import com.github.matidominati.medicalclinic.model.entity.Patient;
import com.github.matidominati.medicalclinic.model.entity.Visit;
import com.github.matidominati.medicalclinic.model.enums.VisitType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class VisitMapperTest {
    VisitMapper visitMapper = Mappers.getMapper(VisitMapper.class);

    @ParameterizedTest
    @MethodSource("correctVisitData")
    void MapVisitToVisitDto_CorrectVisitData_VisitDtoReturned(Visit visit) {
        var result = visitMapper.visitToVisitDtoSafe(visit);

        assertAll(
                () -> assertEquals(visit.getId(), result.getId()),
                () -> assertEquals(visit.getInstitution().getName(), result.getInstitutionName()),
                () -> assertEquals(visit.getInstitution().getAddress(), result.getInstitutionAddress()),
                () -> assertEquals(visit.getDoctor().getId(), result.getDoctorId()),
                () -> assertEquals(visit.getPrice(), result.getPrice()),
                () -> assertEquals(visit.getStartDateTime(), result.getStartDateTime()),
                () -> assertEquals(visit.getEndDateTime(), result.getEndDateTime()),
                () -> assertEquals(visit.getDate(), result.getDate()),
                () -> assertEquals(visit.getStatus(), result.getStatus())
        );
    }

    @ParameterizedTest
    @MethodSource("incorrectVisitData")
    void MapVisitToVisitDto_IncorrectVisitData_VisitDtoNotReturned(Visit visit) {
        assertThrows(DataNotFoundException.class, () -> visitMapper.visitToVisitDtoSafe(visit));
    }

    public static Stream<Arguments> incorrectVisitData() {
        Visit visit1 = Visit.builder()
                .build();
        Visit visit2 = null;
        Visit visit3 = Visit.builder()
                .price(BigDecimal.valueOf(150))
                .status(VisitType.CREATED)
                .date(LocalDateTime.of(2024, 10, 10, 15, 15))
                .startDateTime(LocalDateTime.of(2024, 10, 10, 15, 15))
                .endDateTime(LocalDateTime.of(2024, 10, 10, 15, 45))
                .institution(Institution.builder()
                        .id(1L)
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
        Visit visit4 = Visit.builder()
                .id(1L)
                .price(BigDecimal.valueOf(150))
                .status(VisitType.CREATED)
                .date(LocalDateTime.of(2024, 10, 10, 15, 15))
                .startDateTime(LocalDateTime.of(2024, 10, 10, 15, 15))
                .endDateTime(LocalDateTime.of(2024, 10, 10, 15, 45))
                .institution(Institution.builder()
                        .id(1L)
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
        Visit visit5 = Visit.builder()
                .id(1L)
                .price(BigDecimal.valueOf(150))
                .status(VisitType.CREATED)
                .date(LocalDateTime.of(2024, 10, 10, 15, 15))
                .startDateTime(LocalDateTime.of(2024, 10, 10, 15, 15))
                .endDateTime(LocalDateTime.of(2024, 10, 10, 15, 45))
                .patient(Patient.builder()
                        .id(1L)
                        .firstName("Andrzej")
                        .lastName("Golota")
                        .build())
                .build();
        Visit visit6 = Visit.builder()
                .id(1L)
                .price(BigDecimal.valueOf(150))
                .status(VisitType.CREATED)
                .date(LocalDateTime.of(2024, 10, 10, 15, 15))
                .startDateTime(LocalDateTime.of(2024, 10, 10, 15, 15))
                .endDateTime(LocalDateTime.of(2024, 10, 10, 15, 45))
                .build();

        return Stream.of(
                Arguments.of(visit1),
                Arguments.of(visit2),
                Arguments.of(visit3),
                Arguments.of(visit4),
                Arguments.of(visit5),
                Arguments.of(visit6)
        );
    }

    public static Stream<Arguments> correctVisitData() {
        Visit visit1 = Visit.builder()
                .id(1L)
                .price(BigDecimal.valueOf(150))
                .status(VisitType.CREATED)
                .date(LocalDateTime.of(2024, 10, 10, 15, 15))
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
        Visit visit2 = Visit.builder()
                .id(1L)
                .price(BigDecimal.valueOf(150))
                .status(VisitType.CREATED)
                .date(LocalDateTime.of(2024, 10, 10, 15, 15))
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
                .build();
        Visit visit3 = Visit.builder()
                .id(1L)
                .price(BigDecimal.valueOf(150))
                .status(VisitType.CREATED)
                .date(LocalDateTime.of(2024, 10, 10, 15, 15))
                .startDateTime(LocalDateTime.of(2024, 10, 10, 15, 15))
                .endDateTime(LocalDateTime.of(2024, 10, 10, 15, 45))
                .institution(Institution.builder()
                        .name("NFZ")
                        .address("Warszawa")
                        .build())
                .doctor(Doctor.builder()
                        .id(1L)
                        .build())
                .build();

        return Stream.of(
                Arguments.of(visit1),
                Arguments.of(visit2),
                Arguments.of(visit3)
        );
    }
}
