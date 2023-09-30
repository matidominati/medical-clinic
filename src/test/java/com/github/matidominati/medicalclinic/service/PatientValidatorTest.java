package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.IncorrectNameException;
import com.github.matidominati.medicalclinic.exception.IncorrectPhoneNumberException;
import com.github.matidominati.medicalclinic.model.Patient;
import com.github.matidominati.medicalclinic.repository.PatientRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class PatientValidatorTest {

    @Mock
    PatientValidator patientValidator;

    @Test
    void checkPatientEditableData_ValidData() {
        Patient patient1 = new Patient("andrzej.golota@gmail.com", "andrzej1", "123456", "Andrzej",
                "Golota", "99999999", LocalDate.of(1960, 5, 10));

        Assertions.assertDoesNotThrow(() -> patientValidator.checkPatientEditableData(patient1));
    }

    @Test
    void checkPatientUneditableData_ValidData(){
        Patient patient1 = new Patient("andrzej.golota@gmail.com", "andrzej1", "123456", "Andrzej",
                "Golota", "99999999", LocalDate.of(1960, 5, 10));

        Assertions.assertDoesNotThrow(() -> patientValidator.checkPatientUneditableData(patient1));
    }

    @Test
    void checkPatientPassword_ValidPassword(){
        Patient patient1 = new Patient("andrzej.golota@gmail.com", "andrzej1oo", "123456", "Andrzej",
                "Golota", "99999999", LocalDate.of(1960, 5, 10));

        Assertions.assertTrue(patientValidator.checkPatientPassword(patient1));
    }
}

