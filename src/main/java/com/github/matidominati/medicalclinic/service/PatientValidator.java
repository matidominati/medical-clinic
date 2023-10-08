package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.enity.Patient;
import com.github.matidominati.medicalclinic.exception.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class PatientValidator {
    public void checkPatientEditableData(Patient patient) {
        if (patient.getFirstName() == null || patient.getFirstName().isEmpty()) {
            throw new IncorrectNameException("First name cannot be null");
        }
        if (patient.getLastName() == null || patient.getLastName().isEmpty()) {
            throw new IncorrectNameException("Last name cannot be null");
        }
        if (patient.getPhoneNumber() == null || patient.getPhoneNumber().isEmpty() || patient.getPhoneNumber().length() < 9) {
            throw new IncorrectPhoneNumberException("Phone number must consist of nine digits");
        }
        checkPatientPassword(patient);
    }

    public void checkPatientUneditableData(Patient patient) {
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

    public void checkPatientData(Patient patient) {
        checkPatientUneditableData(patient);
        checkPatientEditableData(patient);

    }

    public boolean checkPatientPassword(Patient patient) {
        if (patient.getPassword() == null || patient.getPassword().isEmpty()) {
            throw new IncorrectPasswordException("Incorrect password provided");
        }
        if (patient.getPassword().equals(patient.getFirstName()) || patient.getPassword().equals(patient.getLastName())) {
            throw new IncorrectPasswordException("Incorrect password provided");
        }
        if (patient.getPassword().length() < 6) {
            throw new IncorrectPasswordException("Password must consist of more than six characters.");
        }
        return true;
    }

}
