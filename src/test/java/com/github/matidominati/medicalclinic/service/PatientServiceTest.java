package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.ChangeIdException;
import com.github.matidominati.medicalclinic.exception.DataAlreadyExistsException;
import com.github.matidominati.medicalclinic.exception.DataNotFoundException;
import com.github.matidominati.medicalclinic.exception.IncorrectPasswordException;
import com.github.matidominati.medicalclinic.mapper.PatientMapper;
import com.github.matidominati.medicalclinic.model.dto.PatientDto;
import com.github.matidominati.medicalclinic.model.entity.Patient;
import com.github.matidominati.medicalclinic.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PatientServiceTest {

    PatientValidator patientValidator;
    PatientRepository patientRepository;
    PatientMapper patientMapper;
    PatientService patientService;

    @BeforeEach
    void setup() {
        this.patientValidator = Mockito.mock(PatientValidator.class);
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.patientMapper = Mappers.getMapper(PatientMapper.class);
        this.patientService = new PatientService(patientValidator, patientRepository, patientMapper);
    }

    @Test
    void getAllPatients_PatientsExists_PatientsReturned() {
        Patient patient1 = new Patient(1,"andrzej.golota@gmail.com", "andrzej1", "123456", "Andrzej",
                "Golota", "99999999", LocalDate.of(1960, 5, 10));
        Patient patient2 = new Patient(2,"zbigniew.durka@gmail.com", "zbyszek1", "124568", "Zbigniew",
                "Durka", "542912421", LocalDate.of(1970, 1, 12));

        List<Patient> expectedPatients = new ArrayList<>();
        expectedPatients.add(patient1);
        expectedPatients.add(patient2);

        when(patientRepository.findAll()).thenReturn(expectedPatients);

        PatientDto patientDto1 = patientMapper.patientToPatientDto(patient1);
        PatientDto patientDto2 = patientMapper.patientToPatientDto(patient2);

        List<PatientDto> expectedPatientsDto = new ArrayList<>();

        expectedPatientsDto.add(patientDto1);
        expectedPatientsDto.add(patientDto2);

        List<PatientDto> result = patientService.getAllPatients();

        assertEquals(expectedPatientsDto, result);
    }

    @Test
    void getPatient_EmailCorrect_PatientReturned() {
        Patient patient = new Patient(1,"patient.patient@gmail.com", "aa", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));

        when(patientRepository.findByEmail(patient.getEmail())).thenReturn(Optional.of(patient));
        PatientDto result = patientService.getPatient("patient.patient@gmail.com");

        assertEquals("patient.patient@gmail.com", result.getEmail());
        assertEquals("bb", result.getFirstName());
        assertEquals("bb", result.getFirstName());
        assertEquals("cc", result.getLastName());
        assertEquals("124", result.getPhoneNumber());
        assertEquals(LocalDate.of(1999, 2, 1), result.getBirthDate());
    }

    @Test
    void getPatient_PatientEmailNotFound_PatientNotReturned() {
        Patient patient = new Patient(1,"patient.patient@gmail.com", "aa", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> patientService.getPatient("patient.patient@gmail.com"));

        assertEquals("Patient with the provided email does not exists", exception.getMessage());
    }

    @Test
    void addPatient_PatientDataCorrect_PatientReturned() {
        Patient patient = new Patient(1,"patient.patient@gmail.com", "aa", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));

        when(patientRepository.findByEmail(patient.getEmail())).thenReturn(Optional.empty());

        PatientDto result = patientService.addPatient(patient);

        assertEquals("patient.patient@gmail.com", result.getEmail());
        assertEquals("bb", result.getFirstName());
        assertEquals("bb", result.getFirstName());
        assertEquals("cc", result.getLastName());
        assertEquals("124", result.getPhoneNumber());
        assertEquals(LocalDate.of(1999, 2, 1), result.getBirthDate());
        verify(patientRepository).save(patient);
    }

    @Test
    void addPatient_PatientDataIncorrect_PatientNotReturned() {
        Patient patient = new Patient(1,"patient.patient@gmail.com", "aa", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));

        when(patientRepository.findByEmail(patient.getEmail())).thenReturn(Optional.of(patient));

        DataAlreadyExistsException exception = assertThrows(DataAlreadyExistsException.class, () -> patientService.addPatient(patient));
        assertEquals("Patient with given email exists", exception.getMessage());
    }

    @Test
    void deletePatient_PatientDataCorrect_DeletedPatient() {
        Patient patientToDelete = new Patient(1,"patient.patient@gmail.com", "aa", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));

        when(patientRepository.findByEmail(patientToDelete.getEmail())).thenReturn(Optional.of(patientToDelete));

        patientService.deletePatient(patientToDelete.getEmail());

        verify(patientRepository, times(1)).delete(patientToDelete);
    }

    @Test
    void deletePatient_PatientDataIncorrect_PatientNotDeleted() {
        Patient patientToDelete = new Patient(1,"patient.patient@gmail.com", "aa", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));

        when(patientRepository.findByEmail(patientToDelete.getEmail())).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> patientService.deletePatient(patientToDelete.getEmail()));

        assertEquals("The patient with the given email address does not exists in the database", exception.getMessage());
    }

    @Test
    void updatePatient_PatientDataCorrect_UpdatePatient() {
        Patient patientOriginal = new Patient(1,"patient.patient@gmail.com", "aa", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));
        Patient patientUpdated = new Patient(1,"patient.patient@gmail.com", "bb", "123", "cc",
                "dd", "55", LocalDate.of(1999, 2, 1));

        when(patientRepository.findByEmail(patientOriginal.getEmail())).thenReturn(Optional.of(patientOriginal));

        PatientDto result = patientService.updatePatient(patientOriginal.getEmail(), patientUpdated);

        verify(patientRepository).findByEmail(patientOriginal.getEmail());
        verify(patientRepository).save(patientUpdated);
        assertEquals("cc", result.getFirstName());
        assertEquals("dd", result.getLastName());
        assertEquals("55", result.getPhoneNumber());
    }

    @Test
    void updatePatient_PatientDataIncorrect_PatientNotUpdated() {
        Patient patientToUpdate = new Patient(1,"patient.patient@gmail.com", "aa", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));
        Patient patientUpdated = new Patient(1,"patient.patient@gmail.com", "bb", "123", "cc",
                "dd", "55", LocalDate.of(1999, 2, 1));

        when(patientRepository.findByEmail(patientToUpdate.getEmail())).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> patientService.updatePatient(patientToUpdate.getEmail(), patientUpdated));

        assertEquals("Patient with the provided email does not exists", exception.getMessage());
    }

    @Test
    void updatePatient_PatientIdChanged_PatientNotUpdated() {
        Patient patientToUpdate = new Patient(1,"patient.patient@gmail.com", "aa", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));
        Patient patientUpdated = new Patient(1,"patient.patient@gmail.com", "bb", "1234", "cc",
                "dd", "55", LocalDate.of(1999, 2, 1));

        when(patientRepository.findByEmail(patientToUpdate.getEmail())).thenReturn(Optional.of(patientToUpdate));

        ChangeIdException exception = assertThrows(ChangeIdException.class, () -> patientService.updatePatient(patientToUpdate.getEmail(), patientUpdated));

        assertEquals("Changing ID number is not allowed!", exception.getMessage());
    }

    @Test
    void changePassword_EmailCorrect_PasswordChanged() {
        Patient patientOriginal = new Patient(1,"patient.patient@gmail.com", "aa", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));
        Patient patientUpdated = new Patient(1,"patient.patient@gmail.com", "patient1", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));

        when(patientRepository.findByEmail(patientOriginal.getEmail())).thenReturn(Optional.of(patientOriginal));
        when(patientValidator.isPatientPasswordValid(patientUpdated)).thenReturn(true);

        patientService.changePassword(patientOriginal.getEmail(), patientUpdated);

        verify(patientValidator, times(1)).isPatientPasswordValid(patientUpdated);
        verify(patientRepository).save(patientUpdated);
    }

    @Test
    void changePassword_EmailIncorrect_PasswordNotChanged() {
        Patient patientToChangePassword = new Patient(1,"patient.patient@gmail.com", "aa", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));
        Patient updatedPatient = new Patient(1,"patient.patient@gmail.com", "patient1", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));

        when(patientRepository.findByEmail(patientToChangePassword.getEmail())).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> patientService.changePassword(patientToChangePassword.getEmail(), updatedPatient));

        assertEquals("Patient with the provided email does not exists", exception.getMessage());
    }

    @Test
    void changePassword_NewPasswordIncorrect_PasswordNotChanged() {
        Patient patientToChangePassword = new Patient(1,"patient.patient@gmail.com", "aabcdef", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));
        Patient updatedPatient = new Patient(1,"patient.patient@gmail.com", "aabcdef", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));

        when(patientRepository.findByEmail(patientToChangePassword.getEmail())).thenReturn(Optional.of(patientToChangePassword));

        IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class, () -> patientService.changePassword(patientToChangePassword.getEmail(), updatedPatient));

        assertEquals("New password cannot be the same as the old password", exception.getMessage());
    }
}
