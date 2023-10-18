package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.mapper.PatientMapper;
import com.github.matidominati.medicalclinic.model.dto.CreatePatientCommand;
import com.github.matidominati.medicalclinic.model.dto.EditPatientCommand;
import com.github.matidominati.medicalclinic.model.dto.PatientDto;
import com.github.matidominati.medicalclinic.model.entity.Patient;
import com.github.matidominati.medicalclinic.exception.DataAlreadyExistsException;
import com.github.matidominati.medicalclinic.exception.DataNotFoundException;
import com.github.matidominati.medicalclinic.exception.IncorrectPasswordException;
import com.github.matidominati.medicalclinic.model.entity.User;
import com.github.matidominati.medicalclinic.repository.PatientRepository;
import com.github.matidominati.medicalclinic.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final CreatePatientDataValidator createPatientDataValidator;
    private final EditPatientDataValidator editPatientDataValidator;
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserRepository userRepository;

    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::patientToPatientDto)
                .collect(Collectors.toList());
    }

    public PatientDto getPatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Patient with the provided ID does not exists"));
        return patientMapper.patientToPatientDto(patient);
    }

    @Transactional
    public PatientDto addPatient(CreatePatientCommand patientCommand) {
        Patient patient = Patient.create(patientCommand);
        createPatientDataValidator.checkPatientData(patientCommand);
        Optional<User> optionalUser = userRepository.findByEmail(patient.getUser().getEmail());
        if (optionalUser.isPresent()) {
            throw new DataAlreadyExistsException("User with given email exists");
        }
        patientRepository.save(patient);
        return patientMapper.patientToPatientDto(patient);
    }

    @Transactional
    public void deletePatient(Long id) {
        Optional<Patient> patientToDelete = patientRepository.findById(id);
        if (patientToDelete.isEmpty()) {
            throw new DataNotFoundException("The patient with the given ID address does not exists in the database");
        }
        patientRepository.delete(patientToDelete.get());
    }

    @Transactional
    public PatientDto updatePatient(Long id, EditPatientCommand updatedPatient) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Patient with the provided ID does not exists"));

        if (updatedPatient.getEmail() != null && !updatedPatient.getEmail().isEmpty()) {
            patient.getUser().setEmail(updatedPatient.getEmail());
        }
        if (updatedPatient.getPassword() != null && !updatedPatient.getPassword().isEmpty()) {
            if (editPatientDataValidator.isPatientPasswordValid(updatedPatient)) {
                patient.getUser().setPassword(updatedPatient.getPassword());
            }
        }
        if (updatedPatient.getFirstName() != null && !updatedPatient.getFirstName().isEmpty()) {
            patient.setFirstName(updatedPatient.getFirstName());
        }
        if (updatedPatient.getLastName() != null && !updatedPatient.getLastName().isEmpty()) {
            patient.setLastName(updatedPatient.getLastName());
        }
        if (updatedPatient.getPhoneNumber() != null && !updatedPatient.getPhoneNumber().isEmpty()) {
            if (editPatientDataValidator.isPatientPhoneNumberValid(updatedPatient)) {
                patient.setPhoneNumber(updatedPatient.getPhoneNumber());
            }
        }
            patientRepository.save(patient);
            System.out.println("Patient data has been updated.");
            return patientMapper.patientToPatientDto(patient);
        }

        @Transactional
        public PatientDto changePassword (Long id, EditPatientCommand updatedPatient){
            Patient patientToChangePassword = patientRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Patient with the provided ID does not exists"));
            if (patientToChangePassword.getUser().getPassword().equals(updatedPatient.getPassword())) {
                throw new IncorrectPasswordException("New password cannot be the same as the old password");
            }
            editPatientDataValidator.isPatientPasswordValid(updatedPatient);
            patientToChangePassword.getUser().setPassword((updatedPatient.getPassword()));

            patientRepository.save(patientToChangePassword);
            return patientMapper.patientToPatientDto(patientToChangePassword);
        }
    }
