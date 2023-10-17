package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.model.dto.CreatePatientCommand;
import com.github.matidominati.medicalclinic.exception.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class CreatePatientDataValidator {
    public void checkPatientEditableData(CreatePatientCommand patient) {

        if (patient.getFirstName() == null || patient.getFirstName().isEmpty()) {
            throw new IncorrectNameException("First name cannot be null");
        }
        if (patient.getLastName() == null || patient.getLastName().isEmpty()) {
            throw new IncorrectNameException("Last name cannot be null");
        }
        if (!isPatientPasswordValid(patient)) {
            throw new IncorrectPasswordException("Incorrect password provided");
        }
        if (patient.getPhoneNumber() == null || patient.getPhoneNumber().isEmpty() || patient.getPhoneNumber().length() < 9) {
            throw new IncorrectPhoneNumberException("Phone number must consist of nine digits");
        }

    }

    public void checkPatientUneditableData(CreatePatientCommand patient) {
        if (patient.getEmail() == null || patient.getEmail().isEmpty()) {
            throw new IncorrectEmailException("Incorrect email provided");
        }
        if (patient.getIdCardNo() == null || patient.getIdCardNo().isEmpty()) {
            throw new ChangeIdException("Incorrect Card ID number provided");
        }
        if (patient.getBirthDate() == null || patient.getBirthDate().isAfter(LocalDate.now())) {
            throw new IncorrectDateException("Incorrect birth date provided");
        }
    }

    public void checkPatientData(CreatePatientCommand patient) {
        checkPatientUneditableData(patient);
        checkPatientEditableData(patient);
    }

    public boolean isPatientPasswordValid(CreatePatientCommand patient) {
        if (patient.getPassword() == null || patient.getPassword().isEmpty()) {
            return false;

        }
        if (patient.getPassword().equals(patient.getFirstName()) || patient.getPassword().equals(patient.getLastName())) {
            return false;
        }
        return patient.getPassword().length() > 6;
    }
}
