package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.*;
import com.github.matidominati.medicalclinic.model.dto.CreatePatientCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CreatePatientDataValidatorTest {
    @InjectMocks
    CreatePatientDataValidator createPatientDataValidator;
    @Test
    void checkPatientEditableData_ValidData() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .firstName("Andrzej")
                .lastName("Golota")
                .password("andrzej1")
                .phoneNumber("999-999-999")
                .build();

        assertDoesNotThrow(() -> createPatientDataValidator.checkPatientEditableData(patient));
    }

    @Test
    public void checkPatientEditableData_NullFirstName() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .firstName(null)
                .lastName("Golota")
                .password("andrzej1")
                .phoneNumber("999-999-999")
                .build();

        IncorrectNameException exception = assertThrows(IncorrectNameException.class, () -> createPatientDataValidator.checkPatientEditableData(patient));
        assertEquals("First name cannot be null", exception.getMessage());
    }

    @Test
    public void checkPatientEditableData_EmptyFirstName() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .firstName("")
                .lastName("Golota")
                .password("andrzej1")
                .phoneNumber("999-999-999")
                .build();

        IncorrectNameException exception = assertThrows(IncorrectNameException.class, () -> createPatientDataValidator.checkPatientEditableData(patient));
        assertEquals("First name cannot be null", exception.getMessage());
    }

    @Test
    public void checkPatientEditableData_NullLastName() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .firstName("Andrzej")
                .lastName(null)
                .phoneNumber("999-999-999")
                .build();

        IncorrectNameException exception = assertThrows(IncorrectNameException.class, () -> createPatientDataValidator.checkPatientEditableData(patient));
        assertEquals("Last name cannot be null", exception.getMessage());
    }

    @Test
    public void checkPatientEditableData_EmptyLastName() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .firstName("Andrzej")
                .lastName("")
                .phoneNumber("999-999-999")
                .build();

        IncorrectNameException exception = assertThrows(IncorrectNameException.class, () -> createPatientDataValidator.checkPatientEditableData(patient));
        assertEquals("Last name cannot be null", exception.getMessage());
    }

    @Test
    public void checkPatientEditableData_InvalidPhoneNumber() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .firstName("Andrzej")
                .lastName("Golota")
                .password("andrzejek123")
                .phoneNumber("999")
                .build();

        IncorrectPhoneNumberException exception = assertThrows(IncorrectPhoneNumberException.class, () -> createPatientDataValidator.checkPatientEditableData(patient));
        assertEquals("Phone number must consist of nine digits", exception.getMessage());
    }

    @Test
    void checkPatientUneditableData_ValidData() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .email("andrzej.gotola@gmail.com")
                .idCardNo("123456")
                .birthDate(LocalDate.of(1960,5,10))
                .build();

        assertDoesNotThrow(() -> createPatientDataValidator.checkPatientUneditableData(patient));
    }

    @Test
    void checkPatientUneditableData_NullEmail() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .email(null)
                .idCardNo("123456")
                .birthDate(LocalDate.of(1960,5,10))
                .build();

        IncorrectEmailException exception = assertThrows(IncorrectEmailException.class, () -> createPatientDataValidator.checkPatientUneditableData(patient));
        assertEquals("Incorrect email provided", exception.getMessage());
    }

    @Test
    void checkPatientUneditableData_EmptyEmail() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .email("")
                .idCardNo("123456")
                .birthDate(LocalDate.of(1960,5,10))
                .build();

        IncorrectEmailException exception = assertThrows(IncorrectEmailException.class, () -> createPatientDataValidator.checkPatientUneditableData(patient));
        assertEquals("Incorrect email provided", exception.getMessage());
    }

    @Test
    void checkPatientUneditableData_NullIdCardNo() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .email("andrzej.gotola@gmail.com")
                .idCardNo(null)
                .birthDate(LocalDate.of(1960,5,10))
                .build();

        ChangeIdException exception = assertThrows(ChangeIdException.class, () -> createPatientDataValidator.checkPatientUneditableData(patient));
        assertEquals("Incorrect Card ID number provided", exception.getMessage());
    }

    @Test
    void checkPatientUneditableData_EmptyIdCardNo() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .email("andrzej.gotola@gmail.com")
                .idCardNo("")
                .birthDate(LocalDate.of(1960,5,10))
                .build();

        ChangeIdException exception = assertThrows(ChangeIdException.class, () -> createPatientDataValidator.checkPatientUneditableData(patient));
        assertEquals("Incorrect Card ID number provided", exception.getMessage());
    }

    @Test
    void checkPatientUneditableData_NullBirthDate() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .email("andrzej.gotola@gmail.com")
                .idCardNo("123456")
                .birthDate(null)
                .build();

        IncorrectDateException exception = assertThrows(IncorrectDateException.class, () -> createPatientDataValidator.checkPatientUneditableData(patient));
        assertEquals("Incorrect birth date provided", exception.getMessage());
    }

    @Test
    void checkPatientUneditableData_BirthDateAfterToday() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .email("andrzej.gotola@gmail.com")
                .idCardNo("123456")
                .birthDate(LocalDate.now().plusDays(1))
                .build();

        IncorrectDateException exception = assertThrows(IncorrectDateException.class, () -> createPatientDataValidator.checkPatientUneditableData(patient));
        assertEquals("Incorrect birth date provided", exception.getMessage());
    }

    @Test
    void checkPatientPassword_ValidPassword() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .password("ANGO12456")
                .build();
        assertTrue(createPatientDataValidator.isPatientPasswordValid(patient));
    }

    @Test
    void checkPatientPassword_NullPassword() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .password(null)
                .build();

        assertFalse(createPatientDataValidator.isPatientPasswordValid(patient));
    }

    @Test
    void checkPatientPassword_EmptyPassword() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .password("")
                .build();

        assertFalse(createPatientDataValidator.isPatientPasswordValid(patient));
    }

    @Test
    void checkPatientPassword_PasswordIsSameAsFirstName() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .firstName("Andrzej")
                .password("Andrzej")
                .build();

        assertFalse(createPatientDataValidator.isPatientPasswordValid(patient));
    }

    @Test
    void checkPatientPassword_PasswordIsSameAsLastName() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .lastName("Golota")
                .password("Golota")
                .build();

        assertFalse(createPatientDataValidator.isPatientPasswordValid(patient));
    }

    @Test
    void checkPatientPassword_PasswordTooShort() {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .password("ANGO1")
                .build();

        assertFalse(createPatientDataValidator.isPatientPasswordValid(patient));
    }
}

