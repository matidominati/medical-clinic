package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.DataAlreadyExistsException;
import com.github.matidominati.medicalclinic.exception.DataNotFoundException;
import com.github.matidominati.medicalclinic.mapper.DoctorMapper;
import com.github.matidominati.medicalclinic.model.dto.DoctorDto;
import com.github.matidominati.medicalclinic.model.dto.InstitutionDto;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateDoctorCommand;
import com.github.matidominati.medicalclinic.model.dto.commandDto.editCommand.EditDoctorCommand;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.model.entity.Institution;
import com.github.matidominati.medicalclinic.model.entity.User;
import com.github.matidominati.medicalclinic.repository.DoctorRepository;
import com.github.matidominati.medicalclinic.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DoctorServiceTest {
    DoctorRepository doctorRepository;
    UserRepository userRepository;
    DoctorMapper doctorMapper;
    DoctorService doctorService;

    @BeforeEach
    void setup() {
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);
        this.doctorService = new DoctorService(doctorRepository, userRepository, doctorMapper);
    }

    @Test
    void getAllDoctors_DoctorsExist_DoctorsReturned() {
        List<Institution> institutions = new ArrayList<>();
        Institution institution = new Institution();
        institutions.add(institution);
        Doctor doctorA = Doctor.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .specialization("kardiolog")
                .phoneNumber("123-456-789")
                .institutions(institutions)
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();
        Doctor doctorB = Doctor.builder()
                .id(2L)
                .firstName("Andrzej")
                .lastName("Nowak")
                .specialization("onkolog")
                .phoneNumber("123-456-999")
                .institutions(institutions)
                .user(User.builder()
                        .email("andrzej.nowak@gmail.com")
                        .username("Anowak")
                        .password("Anowak123")
                        .build())
                .build();

        List<Doctor> expectedDoctors = List.of(doctorA, doctorB);

        when(doctorRepository.findAll()).thenReturn(expectedDoctors);

        DoctorDto doctorDtoA = doctorMapper.doctorToDoctorDto(doctorA);
        DoctorDto doctorDtoB = doctorMapper.doctorToDoctorDto(doctorB);

        List<DoctorDto> expectedDoctorsDto = List.of(doctorDtoA, doctorDtoB);

        List<DoctorDto> result = doctorService.getAllDoctors();

        assertEquals(expectedDoctorsDto, result);
    }

    @Test
    void getDoctor_IdCorrect_DoctorReturned() {
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .specialization("kardiolog")
                .phoneNumber("123-456-789")
                .institutions(null)
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        DoctorDto doctorDto = doctorMapper.doctorToDoctorDto(doctor);
        DoctorDto result = doctorService.getDoctor(doctor.getId());

        assertEquals(doctorDto.getFirstName(), result.getFirstName());
        assertEquals(doctorDto.getLastName(), result.getLastName());
        assertEquals(doctorDto.getSpecialization(), result.getSpecialization());
    }

    @Test
    void getDoctor_DoctorIdNotFound_DoctorNotReturned() {
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .specialization("kardiolog")
                .phoneNumber("123-456-789")
                .institutions(null)
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> doctorService.getDoctor(doctor.getId()));

        assertEquals("Doctor with the provided ID does not exist in the database", exception.getMessage());
    }

    @Test
    @Transactional
    void addDoctor_DoctorDataCorrect_DoctorReturned() {
        CreateDoctorCommand createDoctor = new CreateDoctorCommand();
        createDoctor.setFirstName("Andrzej");
        createDoctor.setLastName("Nowak");
        createDoctor.setPassword("ABC11234");
        createDoctor.setPhoneNumber("123456789");
        createDoctor.setUsername("Anowak12");
        createDoctor.setInstitutions(null);
        createDoctor.setSpecialization("kardiolog");
        createDoctor.setEmail("andrzej.nowak@gmail.com");

        when(userRepository.findByEmail(createDoctor.getEmail())).thenReturn(Optional.empty());

        Doctor createdDoctor = Doctor.createDoctor(createDoctor);
        doctorRepository.save(createdDoctor);

        DoctorDto result = doctorService.addDoctor(createDoctor);

        assertEquals(createdDoctor.getFirstName(), result.getFirstName());
        assertEquals(createdDoctor.getSpecialization(), result.getSpecialization());
        assertEquals(createdDoctor.getLastName(), result.getLastName());
        verify(doctorRepository).save(createdDoctor);
    }

    @Test
    void addDoctor_DoctorDataIncorrect_DoctorNotReturned() {
        CreateDoctorCommand createDoctor = new CreateDoctorCommand();
        createDoctor.setFirstName("Andrzej");
        createDoctor.setLastName("Nowak");
        createDoctor.setPassword("ABC11234");
        createDoctor.setPhoneNumber("123456789");
        createDoctor.setUsername("Anowak12");
        createDoctor.setInstitutions(null);
        createDoctor.setSpecialization("kardiolog");
        createDoctor.setEmail("andrzej.nowak@gmail.com");

        when(userRepository.findByEmail(createDoctor.getEmail())).thenReturn(Optional.of(new User()));

        DataAlreadyExistsException exception = assertThrows(DataAlreadyExistsException.class,
                () -> doctorService.addDoctor(createDoctor));
        assertEquals("User with given email exists", exception.getMessage());
    }

    @Test
    void deleteDoctor_DoctorDataCorrect_DeletedPatient() {
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .specialization("kardiolog")
                .phoneNumber("123-456-789")
                .institutions(null)
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        doctorService.deleteDoctor(doctor.getId());

        verify(doctorRepository, times(1)).delete(doctor);
    }

    @Test
    void deleteDoctor_DoctorDataIncorrect_DoctorNotDeleted() {
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .specialization("kardiolog")
                .phoneNumber("123-456-789")
                .institutions(null)
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> doctorService.deleteDoctor(doctor.getId()));

        assertEquals("Doctor with the provided ID does not exist in the database", exception.getMessage());
    }

    @Test
    void updateDoctor_DoctorDataCorrect_UpdateDoctor() {
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .specialization("kardiolog")
                .phoneNumber("123-456-789")
                .institutions(null)
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        EditDoctorCommand doctorUpdatedData = EditDoctorCommand.builder()
                .firstName("Mariusz")
                .lastName("Nowak")
                .email("andrzej.golota123@gmail.com")
                .password("Agolota11111")
                .phoneNumber("123-456-789")
                .build();

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        DoctorDto result = doctorService.updateDoctor(doctor.getId(), doctorUpdatedData);

        verify(doctorRepository).findById(doctor.getId());
        verify(doctorRepository).save(doctor);
        assertEquals(doctorUpdatedData.getFirstName(), result.getFirstName());
        assertEquals(doctorUpdatedData.getLastName(), result.getLastName());
    }

    @Test
    void updateDoctor_DoctorDataIncorrect_DoctorNotUpdated() {
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .specialization("kardiolog")
                .phoneNumber("123-456-789")
                .institutions(null)
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();

        EditDoctorCommand doctorUpdatedData = EditDoctorCommand.builder()
                .firstName("Mariusz")
                .lastName("Nowak")
                .email("andrzej.golota123@gmail.com")
                .password("Agolota11111")
                .phoneNumber("123-456-789")
                .build();

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> doctorService.updateDoctor(doctor.getId(), doctorUpdatedData));

        assertEquals("Doctor with the provided ID does not exist in the database", exception.getMessage());
    }
}
