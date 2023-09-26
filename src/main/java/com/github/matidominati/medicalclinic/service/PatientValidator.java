package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.ApiRequestException;
import com.github.matidominati.medicalclinic.model.Patient;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class PatientValidator {
    public void checkPatientEditableData(Patient patient) {
        if (patient.getFirstName() == null) {
            throw new ApiRequestException("First name cannot be null", HttpStatus.BAD_REQUEST);
        }
        if (patient.getLastName() == null) {
            throw new ApiRequestException("Last name cannot be null", HttpStatus.BAD_REQUEST);
        }
        if (patient.getPhoneNumber() == null || patient.getPhoneNumber().length() < 9) {
            throw new ApiRequestException("Phone number must consist of nine digits", HttpStatus.BAD_REQUEST);
        }
        checkPatientPassword(patient);
    }

    public void checkPatientUneditableData(Patient patient) {
        if (patient.getEmail() == null) {
            throw new ApiRequestException("Email cannot be null", HttpStatus.BAD_REQUEST);
        }
        if (patient.getIdCardNo() == null) {
            throw new ApiRequestException("Card ID number cannot be null", HttpStatus.BAD_REQUEST);
        }
        if (patient.getBirthday() == null || patient.getBirthday().isAfter(LocalDate.now())) {
            throw new ApiRequestException("Birthday date is incorrect", HttpStatus.BAD_REQUEST);
        }
    }

    public void checkPatientData(Patient patient) {
        checkPatientEditableData(patient);
        checkPatientUneditableData(patient);
    }

    public void checkPatientPassword(Patient patient) {
        if (patient.getPassword() == null) {
            throw new ApiRequestException("Password cannot be null", HttpStatus.BAD_REQUEST);
        }
        if (patient.getPassword().equals(patient.getFirstName()) || patient.getPassword().equals(patient.getLastName())) {
            throw new ApiRequestException("Password cannot be the same as the first name or last name", HttpStatus.BAD_REQUEST);
        }
        if (patient.getPassword().length() < 6) {
            throw new ApiRequestException("Password must consist of more than six characters.", HttpStatus.BAD_REQUEST);
        }
    }
}
