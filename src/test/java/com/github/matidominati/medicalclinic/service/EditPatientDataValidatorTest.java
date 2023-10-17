package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.IncorrectPasswordException;
import com.github.matidominati.medicalclinic.exception.IncorrectPhoneNumberException;
import com.github.matidominati.medicalclinic.model.dto.EditPatientCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EditPatientDataValidatorTest {

    @InjectMocks
    EditPatientDataValidator editPatientDataValidator;

    @Test
    void checkIfPatientPhoneNumberValid_PhoneNumberTooShort() {
        EditPatientCommand patient = EditPatientCommand.builder()
                .phoneNumber("123")
                .build();

        IncorrectPhoneNumberException exception = assertThrows(IncorrectPhoneNumberException.class, () -> editPatientDataValidator.isPatientPhoneNumberValid(patient));
        assertEquals("Phone number must consist of nine digits", exception.getMessage());
    }

    @Test
    void checkPatientPassword_ValidPassword() {
        EditPatientCommand patient = EditPatientCommand.builder()
                .password("ANGO12456")
                .build();

        assertTrue(editPatientDataValidator.isPatientPasswordValid(patient));
    }

    @Test
    void checkPatientPassword_NullPassword() {
        EditPatientCommand patient = EditPatientCommand.builder()
                .password(null)
                .build();

        IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class, () -> editPatientDataValidator.isPatientPasswordValid(patient));
        assertEquals("Incorrect password provided", exception.getMessage());
    }

    @Test
    void checkPatientPassword_EmptyPassword() {
        EditPatientCommand patient = EditPatientCommand.builder()
                .password("")
                .build();

        IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class, () -> editPatientDataValidator.isPatientPasswordValid(patient));
        assertEquals("Incorrect password provided", exception.getMessage());
    }

    @Test
    void checkPatientPassword_PasswordIsSameAsFirstName() {
        EditPatientCommand patient = EditPatientCommand.builder()
                .firstName("Andrzej")
                .password("Andrzej")
                .build();

        IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class, () -> editPatientDataValidator.isPatientPasswordValid(patient));
        assertEquals("Incorrect password provided", exception.getMessage());

    }

    @Test
    void checkPatientPassword_PasswordIsSameAsLastName() {
        EditPatientCommand patient = EditPatientCommand.builder()
                .lastName("Golota")
                .password("Golota")
                .build();

        IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class, () -> editPatientDataValidator.isPatientPasswordValid(patient));
        assertEquals("Incorrect password provided", exception.getMessage());
    }

    @Test
    void checkPatientPassword_PasswordTooShort() {
        EditPatientCommand patient = EditPatientCommand.builder()
                .password("ANGO1")
                .build();

        IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class, () -> editPatientDataValidator.isPatientPasswordValid(patient));
        assertEquals("Password must consist of more than six characters.", exception.getMessage());
    }
}
