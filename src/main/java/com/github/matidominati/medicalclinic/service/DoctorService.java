package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.DataAlreadyExistsException;
import com.github.matidominati.medicalclinic.exception.DataNotFoundException;
import com.github.matidominati.medicalclinic.exception.IncorrectDataException;
import com.github.matidominati.medicalclinic.mapper.DoctorMapper;
import com.github.matidominati.medicalclinic.model.dto.DoctorDto;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateDoctorCommand;
import com.github.matidominati.medicalclinic.model.dto.commandDto.editCommand.EditDoctorCommand;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.model.entity.Patient;
import com.github.matidominati.medicalclinic.repository.DoctorRepository;
import com.github.matidominati.medicalclinic.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.matidominati.medicalclinic.service.validator.CRUDDataValidator.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DoctorMapper doctorMapper;

    public List<DoctorDto> getAllDoctors() {
        log.info("Search process for all doctors has started");
        List<DoctorDto> doctors = doctorRepository.findAll().stream()
                .map(doctorMapper::doctorToDoctorDto)
                .toList();
        log.info("{} doctors found", doctors.size());
        return doctors;
    }

    public DoctorDto getDoctor(Long id) {
        log.info("Process of searching for a doctor with ID: {} has started", id);
        try {
            Doctor doctor = findByIdOrThrow(id, doctorRepository, Doctor.class);
            log.info("Found doctor with ID: {}", id);
            return doctorMapper.doctorToDoctorDto(doctor);
        } catch (DataNotFoundException e) {
            log.info("Doctor with ID: {} not found", id);
            throw e;
        }
    }

    @Transactional
    public DoctorDto addDoctor(CreateDoctorCommand createDoctor) {
        log.info("Doctor data verification has started. {}", createDoctor);
        try {
            checkData(createDoctor.getFirstName(), createDoctor.getLastName(),
                    createDoctor.getPhoneNumber(), createDoctor.getPassword(), createDoctor.getEmail());
            checkIfDataDoesNotExists(createDoctor.getEmail(), createDoctor.getUsername(), userRepository);
            log.info("Data correct. Process of creating doctor has started. {}", createDoctor);
            Doctor doctor = Doctor.createDoctor(createDoctor);
            doctorRepository.save(doctor);
            log.info("New doctor with ID: {} has been created.", doctor.getId());
            return doctorMapper.doctorToDoctorDto(doctor);
        } catch (IncorrectDataException e) {
            log.info("Incorrect data provided (first name, last name, phone number, password or email");
            throw e;
        } catch (DataAlreadyExistsException ex) {
            log.info("Doctor with provided data already exists in the system");
            throw ex;
        }
    }

    @Transactional
    public void deleteDoctor(Long id) {
        log.info("Process of searching for a doctor with ID: {} has started", id);
        try {
            Doctor doctorToDelete = findByIdOrThrow(id, doctorRepository, Doctor.class);
            doctorRepository.delete(doctorToDelete);
            log.info("Doctor with ID: {} has been removed.", id);
        } catch (DataNotFoundException e) {
            log.info("Doctor with ID: {} not found", id);
            throw e;
        }
    }

    @Transactional
    public DoctorDto updateDoctor(Long id, EditDoctorCommand updatedDoctor) {
        log.info("Process of searching for a doctor with ID: {} has started", id);
        try {
            Doctor doctor = findByIdOrThrow(id, doctorRepository, Doctor.class);
            checkDoctorDataToUpdate(updatedDoctor.getEmail(), updatedDoctor.getPassword(), updatedDoctor.getFirstName(),
                    updatedDoctor.getLastName(), updatedDoctor.getPhoneNumber(), doctor);
            doctorRepository.save(doctor);
            log.debug("Doctor data has been updated. {}", updatedDoctor);
            return doctorMapper.doctorToDoctorDto(doctor);
        } catch (DataNotFoundException e) {
            log.info("Doctor with ID: {} not found", id);
            throw e;
        }
    }
}

