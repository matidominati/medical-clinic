package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.model.Patient;
import com.github.matidominati.medicalclinic.repository.PatientRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {
    @Mock
    PatientValidator patientValidator;
    @Mock
    PatientRepositoryImpl patientRepository;
    @InjectMocks
    PatientService patientService;

    @Test
    void getAllPatients_PatientsExists_PatientsReturned() {
        Patient patient1 = new Patient("andrzej.golota@gmail.com", "andrzej1", "123456", "Andrzej",
                "Golota", "99999999", LocalDate.of(1960, 5, 10));
        Patient patient2 = new Patient("zbigniew.durka@gmail.com", "zbyszek1", "124568", "Zbigniew",
                "Durka", "542912421", LocalDate.of(1970, 1, 12));
        List<Patient> expectedPatients = new ArrayList<>();
        expectedPatients.add(patient1);
        expectedPatients.add(patient2);

        when(patientRepository.getAllPatients()).thenReturn(expectedPatients);

        List<Patient> result = patientService.getAllPatients();

        assertEquals(expectedPatients, result);
    }

    @Test
    void getPatient_EmailCorrect_PatientReturned() {
        Patient patient = new Patient("patient.patient@gmail.com", "aa", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));
        when(patientRepository.findPatientByEmail(patient.getEmail())).thenReturn(Optional.of(patient));

        Patient result = patientService.getPatient("patient.patient@gmail.com");

        assertEquals("patient.patient@gmail.com", result.getEmail());
        assertEquals("aa", result.getPassword());
        assertEquals("123", result.getIdCardNo());
        assertEquals("bb", result.getFirstName());
        assertEquals("bb", result.getFirstName());
        assertEquals("cc", result.getLastName());
        assertEquals("124", result.getPhoneNumber());
        assertEquals(LocalDate.of(1999, 2, 1), result.getBirthDate());
    }

    @Test
    void addPatient_PatientDataCorrect_PatientReturned() {
        Patient patient = new Patient("patient.patient@gmail.com", "aa", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));
        when(patientRepository.findPatientByEmail(patient.getEmail())).thenReturn(Optional.empty());

        Patient result = patientService.addPatient(patient);

        assertEquals("patient.patient@gmail.com", result.getEmail());
        assertEquals("aa", result.getPassword());
        assertEquals("123", result.getIdCardNo());
        assertEquals("bb", result.getFirstName());
        assertEquals("bb", result.getFirstName());
        assertEquals("cc", result.getLastName());
        assertEquals("124", result.getPhoneNumber());
        assertEquals(LocalDate.of(1999, 2, 1), result.getBirthDate());
    }

    @Test
    void deletePatient_PatientDataCorrect_DeletedPatient() {
        Patient patientToDelete = new Patient("patient.patient@gmail.com", "aa", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));
        when(patientRepository.findPatientByEmail(patientToDelete.getEmail())).thenReturn(Optional.of(patientToDelete));

        patientService.deletePatient(patientToDelete.getEmail());

        verify(patientRepository, times(1)).deletePatientByEmail(patientToDelete.getEmail());
    }

    @Test
    void updatePatient_PatientDataCorrect_UpdatePatient() {
        Patient patientToUpdate = new Patient("patient.patient@gmail.com", "aa", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));
        Patient patientUpdated = new Patient("patient.patient@gmail.com", "bb", "123", "cc",
                "dd", "55", LocalDate.of(1999, 2, 1));

        when(patientRepository.findPatientByEmail(patientToUpdate.getEmail())).thenReturn(Optional.of(patientToUpdate));

        Patient result = patientService.updatePatient(patientToUpdate.getEmail(), patientUpdated);

        assertEquals("bb", result.getPassword());
        assertEquals("cc", result.getFirstName());
        assertEquals("dd", result.getLastName());
        assertEquals("55", result.getPhoneNumber());
    }

    @Test
    void changePassword_EmailCorrect_PasswordChanged() {
        Patient patientToChangePassword = new Patient("patient.patient@gmail.com", "aa", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));
        Patient updatedPatient = new Patient("patient.patient@gmail.com", "patient1", "123", "bb",
                "cc", "124", LocalDate.of(1999, 2, 1));

        when(patientRepository.findPatientByEmail(patientToChangePassword.getEmail())).thenReturn(Optional.of(patientToChangePassword));
        when(patientValidator.checkPatientPassword(updatedPatient)).thenReturn(true);

        patientService.changePassword(patientToChangePassword.getEmail(), updatedPatient);

        assertEquals(updatedPatient.getPassword(), patientToChangePassword.getPassword());

        verify(patientValidator, times(1)).checkPatientPassword(updatedPatient);
    }
}
