package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.DataAlreadyExistsException;
import com.github.matidominati.medicalclinic.exception.DataNotFoundException;
import com.github.matidominati.medicalclinic.exception.IncorrectDataException;
import com.github.matidominati.medicalclinic.mapper.PatientMapper;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreatePatientCommand;
import com.github.matidominati.medicalclinic.model.dto.commandDto.editCommand.EditPatientCommand;
import com.github.matidominati.medicalclinic.model.dto.PatientDto;
import com.github.matidominati.medicalclinic.model.entity.Patient;
import com.github.matidominati.medicalclinic.exception.IncorrectPasswordException;
import com.github.matidominati.medicalclinic.repository.PatientRepository;
import com.github.matidominati.medicalclinic.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.matidominati.medicalclinic.service.validator.CRUDDataValidator.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserRepository userRepository;

    public List<PatientDto> getAllPatients() {
        log.info("Search process for all patients has started");
        List<PatientDto> patients = patientRepository.findAll().stream()
                .map(patientMapper::patientToPatientDto)
                .toList();
        log.info("{} patients found", patients.size());
        return patients;
    }

    public PatientDto getPatient(Long id) {
        log.info("Process of searching for a patient with an ID: {} has started", id);
        try {
            Patient patient = findByIdOrThrow(id, patientRepository, Patient.class);
            log.info("Found patient with ID: {}", id);
            return patientMapper.patientToPatientDto(patient);
        } catch (DataNotFoundException e) {
            log.info("Patient with ID: {} not found", id);
            throw e;
        }
    }

    @Transactional
    public PatientDto addPatient(CreatePatientCommand createPatient) {
        log.info("Patient data verification has started. {}", createPatient);
        try {
            checkData(createPatient.getFirstName(), createPatient.getLastName(), createPatient.getPhoneNumber(),
                    createPatient.getPassword(), createPatient.getEmail());
            checkIfDataDoesNotExists(createPatient.getEmail(), createPatient.getUsername(), userRepository);
            log.info("Data correct. Process of creating patient has started. {}", createPatient);
            Patient patient = Patient.createPatient(createPatient);
            patientRepository.save(patient);
            userRepository.save(patient.getUser());
            log.info("New patient with ID: {} has been created.", patient.getId());
            return patientMapper.patientToPatientDto(patient);
        } catch (IncorrectDataException e) {
            log.info("Incorrect data provided (first name, last name, phone number, password or email");
            throw e;
        } catch (DataAlreadyExistsException ex) {
            log.info("Patient with provided data already exists in the system");
            throw ex;
        }
    }

    @Transactional
    public void deletePatient(Long id) {
        log.info("Process of searching for a patient with ID: {} has started", id);
        try {
            Patient patientToDelete = findByIdOrThrow(id, patientRepository, Patient.class);
            patientRepository.delete(patientToDelete);
            log.info("Patient with ID: {} has been removed", id);
        } catch (DataNotFoundException e) {
            log.info("Patient with ID: {} not found", id);
            throw e;
        }
    }

    @Transactional
    public PatientDto updatePatient(Long id, EditPatientCommand updatedPatient) {
        log.info("Process of searching for a patient with ID: {} has started", id);
        try {
            Patient patient = findByIdOrThrow(id, patientRepository, Patient.class);
            checkPatientDataToUpdate(updatedPatient.getEmail(), updatedPatient.getPassword(), updatedPatient.getFirstName(),
                    updatedPatient.getLastName(), updatedPatient.getPhoneNumber(), patient);
            patientRepository.save(patient);
            log.info("Patient data has been updated. {}", updatedPatient);
            return patientMapper.patientToPatientDto(patient);
        } catch (DataNotFoundException e) {
            log.info("Patient with ID: {} not found", id);
            throw e;
        }
    }

    @Transactional
    public PatientDto changePassword(Long id, EditPatientCommand updatedPatient) {
        log.info("Process of searching for a patient with ID: {} has started", id);
        try {
            Patient patientToChangePassword = findByIdOrThrow(id, patientRepository, Patient.class);
            if (patientToChangePassword.getUser().getPassword().equals(updatedPatient.getPassword())) {
                throw new IncorrectPasswordException("New password cannot be the same as the old password");
            }
            isPasswordValid(updatedPatient.getPassword(), updatedPatient.getFirstName(), updatedPatient.getLastName());
            patientToChangePassword.getUser().setPassword((updatedPatient.getPassword()));
            patientRepository.save(patientToChangePassword);
            log.info("Password for the patient ID: {} has been changed", id);
            return patientMapper.patientToPatientDto(patientToChangePassword);
        } catch (DataNotFoundException e) {
            log.info("Patient with ID: {} not found", id);
            throw e;
        } catch (IncorrectPasswordException ex) {
            log.info("Invalid new password provided");
            throw ex;
        }
    }
}
