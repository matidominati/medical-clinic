package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.DataAlreadyExistsException;
import com.github.matidominati.medicalclinic.exception.DataNotFoundException;
import com.github.matidominati.medicalclinic.exception.IncorrectPasswordException;
import com.github.matidominati.medicalclinic.mapper.PatientMapper;
import com.github.matidominati.medicalclinic.model.dto.PatientDto;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreatePatientCommand;
import com.github.matidominati.medicalclinic.model.dto.commandDto.editCommand.EditPatientCommand;
import com.github.matidominati.medicalclinic.model.entity.Patient;
import com.github.matidominati.medicalclinic.model.entity.User;
import com.github.matidominati.medicalclinic.repository.PatientRepository;
import com.github.matidominati.medicalclinic.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PatientServiceTest {
    PatientRepository patientRepository;
    UserRepository userRepository;
    PatientMapper patientMapper;
    PatientService patientService;

    @BeforeEach
    void setup() {
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.patientMapper = Mappers.getMapper(PatientMapper.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.patientService = new PatientService(patientRepository, patientMapper, userRepository);
    }

    @Test
    void getAllPatients_PatientsExists_PatientsReturned() {

        Patient patient1 = Patient.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .idCardNo("123456")
                .phoneNumber("123-456-789")
                .birthDate(LocalDate.of(1960, 5, 10))
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        Patient patient2 = Patient.builder()
                .id(2L)
                .firstName("Zbigniew")
                .lastName("Durka")
                .idCardNo("123444")
                .phoneNumber("999-456-888")
                .birthDate(LocalDate.of(1966, 8, 28))
                .user(User.builder()
                        .email("zbigniew.durka@gmail.com")
                        .username("Zdurka")
                        .password("Zdurka123")
                        .build())
                .build();

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
    void getPatient_IdCorrect_PatientReturned() {
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .idCardNo("123456")
                .phoneNumber("123-456-789")
                .birthDate(LocalDate.of(1960, 5, 10))
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));

        PatientDto patientDto = patientMapper.patientToPatientDto(patient);
        PatientDto result = patientService.getPatient(patient.getId());

        assertEquals(patientDto.getFirstName(), result.getFirstName());
        assertEquals(patientDto.getLastName(), result.getLastName());
        assertEquals(patientDto.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(patientDto.getBirthDate(), result.getBirthDate());
    }

    @Test
    void getPatient_PatientIdNotFound_PatientNotReturned() {
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .idCardNo("123456")
                .phoneNumber("123-456-789")
                .birthDate(LocalDate.of(1960, 5, 10))
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> patientService.getPatient(patient.getId()));

        assertEquals("Patient with the provided ID does not exist in the database", exception.getMessage());
    }

    @Test
    void addPatient_PatientDataCorrect_PatientReturned() {
        CreatePatientCommand patientCommand = new CreatePatientCommand();
        patientCommand.setFirstName("Andrzej");
        patientCommand.setLastName("Golota");
        patientCommand.setPhoneNumber("123456789");
        patientCommand.setUsername("AGlta");
        patientCommand.setPassword("Aglta123");
        patientCommand.setEmail("andrzej.golota@gmail.com");
        CreatePatientCommand.builder();

        when(userRepository.findByEmail(patientCommand.getEmail())).thenReturn(Optional.empty());

        Patient createdPatient = Patient.create(patientCommand);

        PatientDto result = patientService.addPatient(patientCommand);

        assertEquals(createdPatient.getFirstName(), result.getFirstName());
        assertEquals(createdPatient.getLastName(), result.getLastName());
        assertEquals(createdPatient.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(createdPatient.getBirthDate(), result.getBirthDate());
        verify(patientRepository).save(createdPatient);
    }

    @Test
    void addPatient_PatientDataIncorrect_PatientNotReturned() {
        CreatePatientCommand patientCommand = new CreatePatientCommand();
        patientCommand.setFirstName("Andrzej");
        patientCommand.setLastName("Golota");
        patientCommand.setPhoneNumber("123456789");
        patientCommand.setUsername("AGlta");
        patientCommand.setPassword("Aglta123");
        patientCommand.setEmail("andrzej.golota@gmail.com");

        when(userRepository.findByEmail(patientCommand.getEmail())).thenReturn(Optional.of(new User()));

        DataAlreadyExistsException exception = assertThrows(DataAlreadyExistsException.class, () -> patientService.addPatient(patientCommand));
        assertEquals("User with given email exist", exception.getMessage());
    }

    @Test
    void deletePatient_PatientDataCorrect_DeletedPatient() {
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .idCardNo("123456")
                .phoneNumber("123-456-789")
                .birthDate(LocalDate.of(1960, 5, 10))
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        patientService.deletePatient(patient.getId());

        verify(patientRepository, times(1)).delete(patient);
    }

    @Test
    void deletePatient_PatientDataIncorrect_PatientNotDeleted() {
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .idCardNo("123456")
                .phoneNumber("123-456-789")
                .birthDate(LocalDate.of(1960, 5, 10))
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        when(patientRepository.findById(patient.getId())).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> patientService.deletePatient(patient.getId()));

        assertEquals("Patient with the provided ID does not exist in the database", exception.getMessage());
    }

    @Test
    void updatePatient_PatientDataCorrect_UpdatePatient() {
        Patient patientOriginal = Patient.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .idCardNo("123456")
                .phoneNumber("123-456-789")
                .birthDate(LocalDate.of(1960, 5, 10))
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        EditPatientCommand patientUpdatedData = EditPatientCommand.builder()
                .firstName("Mariusz")
                .lastName("Nowak")
                .email("andrzej.golota123@gmail.com")
                .password("Agolota11111")
                .phoneNumber("123-456-789")
                .build();

        when(patientRepository.findById(patientOriginal.getId())).thenReturn(Optional.of(patientOriginal));

        PatientDto result = patientService.updatePatient(patientOriginal.getId(), patientUpdatedData);

        verify(patientRepository).findById(patientOriginal.getId());
        verify(patientRepository).save(patientOriginal);
        assertEquals(patientUpdatedData.getFirstName(), result.getFirstName());
        assertEquals(patientUpdatedData.getLastName(), result.getLastName());
        assertEquals(patientUpdatedData.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    void updatePatient_PatientDataIncorrect_PatientNotUpdated() {
        Patient patientOriginal = Patient.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .idCardNo("123456")
                .phoneNumber("123-456-789")
                .birthDate(LocalDate.of(1960, 5, 10))
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        EditPatientCommand patientUpdatedData = EditPatientCommand.builder()
                .firstName("Mariusz")
                .lastName("Nowak")
                .email("andrzej.golota123@gmail.com")
                .password("Agolota11111")
                .phoneNumber("123-456-789")
                .build();

        when(patientRepository.findById(patientOriginal.getId())).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> patientService.updatePatient(patientOriginal.getId(), patientUpdatedData));

        assertEquals("Patient with the provided ID does not exist in the database", exception.getMessage());
    }

    @Test
    void changePassword_IdCorrect_PasswordChanged() {
        Patient patientOriginal = Patient.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .idCardNo("123456")
                .phoneNumber("123-456-789")
                .birthDate(LocalDate.of(1960, 5, 10))
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        EditPatientCommand patientUpdatedData = EditPatientCommand.builder()
                .password("Agolota11111")
                .build();

        when(patientRepository.findById(patientOriginal.getId())).thenReturn(Optional.of(patientOriginal));

        patientService.changePassword(patientOriginal.getId(), patientUpdatedData);
        verify(patientRepository).save(patientOriginal);
    }

    @Test
    void changePassword_IdIncorrect_PasswordNotChanged() {
        Patient patientOriginal = Patient.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .idCardNo("123456")
                .phoneNumber("123-456-789")
                .birthDate(LocalDate.of(1960, 5, 10))
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        EditPatientCommand patientUpdatedData = EditPatientCommand.builder()
                .password("Agolota11111")
                .build();

        when(patientRepository.findById(patientOriginal.getId())).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> patientService.changePassword(patientOriginal.getId(), patientUpdatedData));

        assertEquals("Patient with the provided ID does not exist in the database", exception.getMessage());
    }

    @Test
    void changePassword_NewPasswordIncorrect_PasswordNotChanged() {
        Patient patientOriginal = Patient.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .idCardNo("123456")
                .phoneNumber("123-456-789")
                .birthDate(LocalDate.of(1960, 5, 10))
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        EditPatientCommand patientUpdatedData = EditPatientCommand.builder()
                .password("Agolota123")
                .build();

        when(patientRepository.findById(patientOriginal.getId())).thenReturn(Optional.of(patientOriginal));

        IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class, () -> patientService.changePassword(patientOriginal.getId(), patientUpdatedData));

        assertEquals("New password cannot be the same as the old password", exception.getMessage());
    }
}
