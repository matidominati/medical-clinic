package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.mapper.PatientMapper;
import com.github.matidominati.medicalclinic.mapper.VisitMapper;
import com.github.matidominati.medicalclinic.model.dto.VisitDto;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreatePatientCommand;
import com.github.matidominati.medicalclinic.model.dto.commandDto.editCommand.EditPatientCommand;
import com.github.matidominati.medicalclinic.model.dto.PatientDto;
import com.github.matidominati.medicalclinic.model.entity.Patient;
import com.github.matidominati.medicalclinic.exception.DataNotFoundException;
import com.github.matidominati.medicalclinic.exception.IncorrectPasswordException;
import com.github.matidominati.medicalclinic.repository.PatientRepository;
import com.github.matidominati.medicalclinic.repository.UserRepository;
import com.github.matidominati.medicalclinic.service.validator.crudValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserRepository userRepository;
    private final VisitMapper visitMapper;

    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::patientToPatientDto)
                .collect(Collectors.toList());
    }

    public PatientDto getPatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Patient with the provided ID does not exist"));
        return patientMapper.patientToPatientDto(patient);
    }

    @Transactional
    public PatientDto addPatient(CreatePatientCommand createPatient) {

        crudValidator.checkData(createPatient.getFirstName(), createPatient.getLastName(), createPatient.getPhoneNumber(),
                createPatient.getPassword(), createPatient.getEmail());
        crudValidator.checkIfDataDoesNotExists(createPatient.getEmail(), createPatient.getUsername(), userRepository);
        Patient patient = Patient.create(createPatient);
        patientRepository.save(patient);
        userRepository.save(patient.getUserData());
        return patientMapper.patientToPatientDto(patient);
    }

    @Transactional
    public void deletePatient(Long id) {
        Optional<Patient> patientToDelete = patientRepository.findById(id);
        if (patientToDelete.isEmpty()) {
            throw new DataNotFoundException("The patient with the given ID does not exist in the database");
        }
        patientRepository.delete(patientToDelete.get());
    }

    @Transactional
    public PatientDto updatePatient(Long id, EditPatientCommand updatedPatient) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Patient with the provided ID does not exist"));
        crudValidator.checkPatientDataToUpdate(updatedPatient.getEmail(), updatedPatient.getPassword(), updatedPatient.getFirstName(),
                updatedPatient.getLastName(), updatedPatient.getPhoneNumber(), patient);
        patientRepository.save(patient);
        System.out.println("Patient data has been updated.");
        return patientMapper.patientToPatientDto(patient);
    }

    @Transactional
    public PatientDto changePassword(Long id, EditPatientCommand updatedPatient) {
        Patient patientToChangePassword = patientRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Patient with the provided ID does not exist"));
        if (patientToChangePassword.getUserData().getPassword().equals(updatedPatient.getPassword())) {
            throw new IncorrectPasswordException("New password cannot be the same as the old password");
        }
        crudValidator.isPasswordValid(updatedPatient.getPassword(), updatedPatient.getFirstName(), updatedPatient.getLastName());
        patientToChangePassword.getUserData().setPassword((updatedPatient.getPassword()));
        patientRepository.save(patientToChangePassword);
        return patientMapper.patientToPatientDto(patientToChangePassword);
    }

    public List<VisitDto> getAllVisits(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Patient with given ID does not exist"));
         List<VisitDto> patientVisits = patient.getVisits().stream()
                .map(visit -> visitMapper.visitToVisitDto(visit))
                .collect(Collectors.toList());
         return patientVisits;
    }
}
