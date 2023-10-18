package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.DataAlreadyExistsException;
import com.github.matidominati.medicalclinic.exception.DataNotFoundException;
import com.github.matidominati.medicalclinic.exception.IncorrectPasswordException;
import com.github.matidominati.medicalclinic.mapper.PatientMapper;
import com.github.matidominati.medicalclinic.model.dto.CreatePatientCommand;
import com.github.matidominati.medicalclinic.model.dto.EditPatientCommand;
import com.github.matidominati.medicalclinic.model.dto.PatientDto;
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

    CreatePatientDataValidator createPatientDataValidator;
    EditPatientDataValidator editPatientDataValidator;
    PatientRepository patientRepository;
    UserRepository userRepository;
    PatientMapper patientMapper;
    PatientService patientService;


    @BeforeEach
    void setup() {
        this.createPatientDataValidator = Mockito.mock(CreatePatientDataValidator.class);
        this.editPatientDataValidator = Mockito.mock(EditPatientDataValidator.class);
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.patientMapper = Mappers.getMapper(PatientMapper.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.patientService = new PatientService(createPatientDataValidator,editPatientDataValidator, patientRepository, patientMapper, userRepository);
    }

    @Test
    void getAllPatients_PatientsExists_PatientsReturned() {

        Patient patient1 = Patient.builder()
                .id(1)
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
                .id(2)
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
    void getPatient_EmailCorrect_PatientReturned() {

        Patient patient = Patient.builder()
                .id(1)
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

        PatientDto result = patientService.getPatient(patient.getId());

        assertEquals(patient.getFirstName(), result.getFirstName());
        assertEquals(patient.getLastName(), result.getLastName());
        assertEquals(patient.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(patient.getBirthDate(), result.getBirthDate());
    }

    @Test
    void getPatient_PatientEmailNotFound_PatientNotReturned() {
        Patient patient = Patient.builder()
                .id(1)
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

        assertEquals("Patient with the provided ID does not exists", exception.getMessage());
    }

    @Test
    void addPatient_PatientDataCorrect_PatientReturned() {
        CreatePatientCommand patientCommand = CreatePatientCommand.builder()
                .firstName("Andrzej")
                .lastName("Golota")
                .idCardNo("123456")
                .phoneNumber("123-456-789")
                .birthDate(LocalDate.of(1960, 5, 10))
                .username("AGlta")
                .password("Aglta123")
                .email("andrzej.golota@gmail.com")
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

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
        CreatePatientCommand patientCommand = CreatePatientCommand.builder()
                .firstName("Andrzej")
                .lastName("Golota")
                .idCardNo("123456")
                .phoneNumber("123-456-789")
                .birthDate(LocalDate.of(1960, 5, 10))
                .username("AGlta")
                .password("Aglta123")
                .email("andrzej.golota@gmail.com")
                .build();

        when(userRepository.findByEmail(patientCommand.getEmail())).thenReturn(Optional.of(new User()));

        DataAlreadyExistsException exception = assertThrows(DataAlreadyExistsException.class, () -> patientService.addPatient(patientCommand));
        assertEquals("User with given email exists", exception.getMessage());
    }

    @Test
    void deletePatient_PatientDataCorrect_DeletedPatient() {
        Patient patient = Patient.builder()
                .id(1)
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
                .id(1)
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

        assertEquals("The patient with the given ID address does not exists in the database", exception.getMessage());
    }

    @Test
    void updatePatient_PatientDataCorrect_UpdatePatient() {
        Patient patientOriginal = Patient.builder()
                .id(1)
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
        assertEquals(patientUpdatedData.getLastName(),result.getLastName());
        assertEquals(patientUpdatedData.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    void updatePatient_PatientDataIncorrect_PatientNotUpdated() {
        Patient patientOriginal = Patient.builder()
                .id(1)
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

        assertEquals("Patient with the provided ID does not exists", exception.getMessage());
    }

    @Test
    void changePassword_IdCorrect_PasswordChanged() {
        Patient patientOriginal = Patient.builder()
                .id(1)
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
        when(editPatientDataValidator.isPatientPasswordValid(patientUpdatedData)).thenReturn(true);

        patientService.changePassword(patientOriginal.getId(), patientUpdatedData);

        verify(editPatientDataValidator, times(1)).isPatientPasswordValid(patientUpdatedData);
        verify(patientRepository).save(patientOriginal);
    }

    @Test
    void changePassword_IdIncorrect_PasswordNotChanged() {
        Patient patientOriginal = Patient.builder()
                .id(1)
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

        assertEquals("Patient with the provided ID does not exists", exception.getMessage());
    }

    @Test
    void changePassword_NewPasswordIncorrect_PasswordNotChanged() {
        Patient patientOriginal = Patient.builder()
                .id(1)
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
