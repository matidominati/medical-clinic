package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.IncorrectPasswordException;
import com.github.matidominati.medicalclinic.exception.IncorrectPhoneNumberException;
import com.github.matidominati.medicalclinic.model.dto.EditPatientCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EditPatientDataValidator {
    public boolean isPatientPhoneNumberValid(EditPatientCommand patient) {
        if (patient.getPhoneNumber().length() < 9) {
            throw new IncorrectPhoneNumberException("Phone number must consist of nine digits");
        }
        return true;
    }

    public boolean isPatientPasswordValid(EditPatientCommand patient) {
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
