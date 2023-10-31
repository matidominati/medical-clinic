package com.github.matidominati.medicalclinic.dataFactory;

import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreatePatientCommand;
import com.github.matidominati.medicalclinic.model.entity.*;
import com.github.matidominati.medicalclinic.model.enums.VisitType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestDataFactory {
    public static Patient createPatient(Long id, String firstName, String lastName, String phoneNumber, String idCardNo, LocalDate birthDate) {
        return Patient.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .idCardNo(idCardNo)
                .phoneNumber(phoneNumber)
                .birthDate(birthDate)
                .build();
    }
    public static Institution createInstitution(Long id, String name, String address, List<Doctor> doctors) {
        return Institution.builder()
                .id(1L)
                .name(name)
                .address(address)
                .doctors(doctors)
                .build();
    }
    public static Doctor createDoctor(Long id, String firstName, String lastName, String specialization,
                                      String phoneNumber, List<Institution> institutions) {
        return Doctor.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .specialization(specialization)
                .phoneNumber(phoneNumber)
                .institutions(new ArrayList<>())
                .build();
    }
    public static Visit createVisit(Long id, VisitType status, BigDecimal price, LocalDateTime startDateTime,
                                    LocalDateTime endDateTime, Doctor doctor, Patient patient, Institution institution) {
        return Visit.builder()
                .id(id)
                .status(VisitType.CREATED)
                .price(price)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .doctor(doctor)
                .patient(patient)
                .institution(institution)
                .build();
    }

}
