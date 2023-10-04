package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.*;
import com.github.matidominati.medicalclinic.model.Patient;
import com.github.matidominati.medicalclinic.repository.PatientRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PatientValidatorTest {

    @InjectMocks
    PatientValidator patientValidator;

    @Test
    void checkPatientEditableData_ValidData() {
        Patient patient1 = new Patient("andrzej.golota@gmail.com", "andrzej1", "123456", "Andrzej",
                "Golota", "999999999", LocalDate.of(1960, 5, 10));

        Assertions.assertDoesNotThrow(() -> patientValidator.checkPatientEditableData(patient1));
    }

    @Test
    public void CheckPatientEditableData_NullFirstName() {

        Patient patient = new Patient("andrzej.golota@gmail.com", "andrzej1", "123456", null,
                "Golota", "999999999", LocalDate.of(1960, 5, 10));

        IncorrectNameException exception = assertThrows(IncorrectNameException.class, () -> patientValidator.checkPatientEditableData(patient));
        assertEquals("First name cannot be null", exception.getMessage());
    }

    @Test
    public void CheckPatientEditableData_EmptyFirstName() {

        Patient patient = new Patient("andrzej.golota@gmail.com", "andrzej1", "123456", "",
                "Golota", "999999999", LocalDate.of(1960, 5, 10));

        IncorrectNameException exception = assertThrows(IncorrectNameException.class, () -> patientValidator.checkPatientEditableData(patient));
        assertEquals("First name cannot be null", exception.getMessage());
    }

    @Test
    public void CheckPatientEditableData_NullLastName() {

        Patient patient = new Patient("andrzej.golota@gmail.com", "andrzej1", "123456", "Andrzej",
                null, "999999999", LocalDate.of(1960, 5, 10));

        IncorrectNameException exception = assertThrows(IncorrectNameException.class, () -> patientValidator.checkPatientEditableData(patient));
        assertEquals("Last name cannot be null", exception.getMessage());
    }

    @Test
    public void CheckPatientEditableData_EmptyLastName() {

        Patient patient = new Patient("andrzej.golota@gmail.com", "andrzej1", "123456", "Andrzej",
                "", "999999999", LocalDate.of(1960, 5, 10));

        IncorrectNameException exception = assertThrows(IncorrectNameException.class, () -> patientValidator.checkPatientEditableData(patient));
        assertEquals("Last name cannot be null", exception.getMessage());
    }

    @Test
    public void CheckPatientEditableData_InvalidPhoneNumber() {

        Patient patient = new Patient("andrzej.golota@gmail.com", "andrzej1", "123456", "Andrzej",
                "Golota", "9", LocalDate.of(1960, 5, 10));

        IncorrectPhoneNumberException exception = assertThrows(IncorrectPhoneNumberException.class, () -> patientValidator.checkPatientEditableData(patient));
        assertEquals("Phone number must consist of nine digits", exception.getMessage());
    }

    @Test
    void checkPatientUneditableData_ValidData() {
        Patient patient = new Patient("andrzej.golota@gmail.com", "andrzej1", "123456", "Andrzej",
                "Golota", "99999999", LocalDate.of(1960, 5, 10));

        Assertions.assertDoesNotThrow(() -> patientValidator.checkPatientUneditableData(patient));
    }

    @Test
    void checkPatientUneditableData_NullEmail() {
        Patient patient = new Patient(null, "andrzej1", "123456", "Andrzej",
                "Golota", "99999999", LocalDate.of(1960, 5, 10));

        IncorrectEmailException exception = assertThrows(IncorrectEmailException.class, () -> patientValidator.checkPatientUneditableData(patient));
        assertEquals("Email cannot be null or empty", exception.getMessage());
    }

    @Test
    void checkPatientUneditableData_EmptyEmail() {
        Patient patient = new Patient("", "andrzej1", "123456", "Andrzej",
                "Golota", "99999999", LocalDate.of(1960, 5, 10));

        IncorrectEmailException exception = assertThrows(IncorrectEmailException.class, () -> patientValidator.checkPatientUneditableData(patient));
        assertEquals("Email cannot be null or empty", exception.getMessage());
    }

    @Test
    void checkPatientUneditableData_NullIdCardNo() {
        Patient patient = new Patient("andrzej.golota@gmail.com", "andrzej1", null, "Andrzej",
                "Golota", "99999999", LocalDate.of(1960, 5, 10));

        ChangeIdException exception = assertThrows(ChangeIdException.class, () -> patientValidator.checkPatientUneditableData(patient));
        assertEquals("Card ID number cannot be null or empty", exception.getMessage());
    }

    @Test
    void checkPatientUneditableData_EmptyIdCardNo() {
        Patient patient = new Patient("andrzej.golota@gmail.com", "andrzej1", "", "Andrzej",
                "Golota", "99999999", LocalDate.of(1960, 5, 10));

        ChangeIdException exception = assertThrows(ChangeIdException.class, () -> patientValidator.checkPatientUneditableData(patient));
        assertEquals("Card ID number cannot be null or empty", exception.getMessage());
    }

    @Test
    void checkPatientUneditableData_NullBirthDate() {
        Patient patient = new Patient("andrzej.golota@gmail.com", "andrzej1", "123456", "Andrzej",
                "Golota", "99999999", null);

        IncorrectDateException exception = assertThrows(IncorrectDateException.class, () -> patientValidator.checkPatientUneditableData(patient));
        assertEquals("Birth date cannot be null or be later than the current one", exception.getMessage());
    }

    @Test
    void checkPatientUneditableData_BirthDateAfterToday() {
        Patient patient = new Patient("andrzej.golota@gmail.com", "andrzej1", "123456", "Andrzej",
                "Golota", "99999999", (LocalDate.now().plusDays(1)));

        IncorrectDateException exception = assertThrows(IncorrectDateException.class, () -> patientValidator.checkPatientUneditableData(patient));
        assertEquals("Birth date cannot be null or be later than the current one", exception.getMessage());
    }

    @Test
    void checkPatientPassword_ValidPassword() {
        Patient patient = new Patient("andrzej.golota@gmail.com", "andrzej1oo", "123456", "Andrzej",
                "Golota", "99999999", LocalDate.of(1960, 5, 10));

        Assertions.assertTrue(patientValidator.checkPatientPassword(patient));
    }

    @Test
    void checkPatientPassword_NullPassword() {
        Patient patient = new Patient("andrzej.golota@gmail.com", null, "123456", "Andrzej",
                "Golota", "99999999", LocalDate.of(1960, 5, 10));

        IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class, () -> patientValidator.checkPatientPassword(patient));
        assertEquals("Password cannot be null or empty", exception.getMessage());
    }

    @Test
    void checkPatientPassword_EmptyPassword() {
        Patient patient = new Patient("andrzej.golota@gmail.com", "", "123456", "Andrzej",
                "Golota", "99999999", LocalDate.of(1960, 5, 10));

        IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class, () -> patientValidator.checkPatientPassword(patient));
        assertEquals("Password cannot be null or empty", exception.getMessage());
    }

    @Test
    void checkPatientPassword_PasswordIsSameAsFirstName() {
        Patient patient = new Patient("andrzej.golota@gmail.com", "Andrzej", "123456", "Andrzej",
                "Golota", "99999999", LocalDate.of(1960, 5, 10));

        IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class, () -> patientValidator.checkPatientPassword(patient));
        assertEquals("Password cannot be the same as the first name or last name", exception.getMessage());
    }

    @Test
    void checkPatientPassword_PasswordIsSameAsLastName() {
        Patient patient = new Patient("andrzej.golota@gmail.com", "Golota", "123456", "Andrzej",
                "Golota", "99999999", LocalDate.of(1960, 5, 10));

        IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class, () -> patientValidator.checkPatientPassword(patient));
        assertEquals("Password cannot be the same as the first name or last name", exception.getMessage());
    }

    @Test
    void checkPatientPassword_PasswordTooShort() {
        Patient patient = new Patient("andrzej.golota@gmail.com", "1234", "123456", "Andrzej",
                "Golota", "99999999", LocalDate.of(1960, 5, 10));

        IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class, () -> patientValidator.checkPatientPassword(patient));
        assertEquals("Password must consist of more than six characters.", exception.getMessage());
    }
}

