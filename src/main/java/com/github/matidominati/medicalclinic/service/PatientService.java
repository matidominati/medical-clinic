package com.github.matidominati.medicalclinic.service;

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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.matidominati.medicalclinic.service.validator.CRUDDataValidator.*;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserRepository userRepository;

    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::patientToPatientDto)
                .collect(Collectors.toList());
    }

    public PatientDto getPatient(Long id) {
        Patient patient = findByIdOrThrow(id, patientRepository, Patient.class);
        return patientMapper.patientToPatientDto(patient);
    }

    @Transactional
    public PatientDto addPatient(CreatePatientCommand createPatient) {
        checkData(createPatient.getFirstName(), createPatient.getLastName(), createPatient.getPhoneNumber(),
                createPatient.getPassword(), createPatient.getEmail());
        checkIfDataDoesNotExists(createPatient.getEmail(), createPatient.getUsername(), userRepository);
        Patient patient = Patient.createPatient(createPatient);
        patientRepository.save(patient);
        userRepository.save(patient.getUser());
        return patientMapper.patientToPatientDto(patient);
    }

    @Transactional
    public void deletePatient(Long id) {
        Patient patientToDelete = findByIdOrThrow(id, patientRepository, Patient.class);
        patientRepository.delete(patientToDelete);
    }

    @Transactional
    public PatientDto updatePatient(Long id, EditPatientCommand updatedPatient) {
        Patient patient = findByIdOrThrow(id, patientRepository, Patient.class);
        checkPatientDataToUpdate(updatedPatient.getEmail(), updatedPatient.getPassword(), updatedPatient.getFirstName(),
                updatedPatient.getLastName(), updatedPatient.getPhoneNumber(), patient);
        patientRepository.save(patient);
        System.out.println("Patient data has been updated.");
        return patientMapper.patientToPatientDto(patient);
    }

    @Transactional
    public PatientDto changePassword(Long id, EditPatientCommand updatedPatient) {
        Patient patientToChangePassword = findByIdOrThrow(id, patientRepository, Patient.class);
        if (patientToChangePassword.getUser().getPassword().equals(updatedPatient.getPassword())) {
            throw new IncorrectPasswordException("New password cannot be the same as the old password");
        }
        isPasswordValid(updatedPatient.getPassword(), updatedPatient.getFirstName(), updatedPatient.getLastName());
        patientToChangePassword.getUser().setPassword((updatedPatient.getPassword()));
        patientRepository.save(patientToChangePassword);
        return patientMapper.patientToPatientDto(patientToChangePassword);
    }
}