package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.model.Patient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class PatientValidator {
    public void checkPatientEditableData(Patient patient) {
        if (patient.getFirstName() == null) {
            throw new IllegalArgumentException("First name cannot be null");
        }
        if (patient.getLastName() == null) {
            throw new IllegalArgumentException("Last name cannot be null");
        }
        if (patient.getPhoneNumber() == null || patient.getPhoneNumber().length() < 9) {
            throw new IllegalArgumentException("Phone number must consist of nine digits");
        }
        checkPatientPassword(patient);
    }

    public void checkPatientUneditableData(Patient patient) {
        if (patient.getEmail() == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (patient.getIdCardNo() == null) {
            throw new IllegalArgumentException("Card ID number cannot be null");
        }
        if (patient.getBirthday() == null || patient.getBirthday().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birthday date is incorrect");
        }
    }

    public void checkPatientData(Patient patient) {
        checkPatientEditableData(patient);
        checkPatientUneditableData(patient);
    }

    public void checkPatientPassword(Patient patient) {
        if (patient.getPassword() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        if (patient.getPassword().equals(patient.getFirstName()) || patient.getPassword().equals(patient.getLastName())) {
            throw new IllegalArgumentException("Password cannot be the same as the first name or last name");
        }
        if (patient.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must consist of more than six characters.");
        }
    }
}
