package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.model.Patient;
import com.github.matidominati.medicalclinic.exception.ChangeIdException;
import com.github.matidominati.medicalclinic.exception.DataAlreadyExistsException;
import com.github.matidominati.medicalclinic.exception.DataNotFoundException;
import com.github.matidominati.medicalclinic.exception.IncorrectPasswordException;
import com.github.matidominati.medicalclinic.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientValidator patientValidator;
    private final PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatient(String email) {
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Patient with the provided email does not exists"));
    }

    @Transactional
    public Patient addPatient(Patient patient) {
        patientValidator.checkPatientData(patient);
        Optional<Patient> optionalPatient = patientRepository.findByEmail(patient.getEmail());
        if (optionalPatient.isPresent()) {
            throw new DataAlreadyExistsException("Patient with given email exists");
        }
        patientRepository.save(patient);
        return patient;
    }

    @Transactional
    public void deletePatient(String email) {
        Optional<Patient> patientToDelete = patientRepository.findByEmail(email);
        if (patientToDelete.isEmpty()) {
            throw new DataNotFoundException("The patient with the given email address does not exists in the database");
        }
        patientRepository.delete(patientToDelete.get());
    }

    @Transactional
    public Patient updatePatient(String email, Patient updatedPatient) {
        patientValidator.checkPatientData(updatedPatient);
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Patient with the provided email does not exists"));
        if (!patient.getIdCardNo().equals(updatedPatient.getIdCardNo())) {
            throw new ChangeIdException("Changing ID number is not allowed!");
        }
        patient.setPassword(updatedPatient.getPassword());
        patient.setFirstName(updatedPatient.getFirstName());
        patient.setLastName(updatedPatient.getLastName());
        patient.setPhoneNumber(updatedPatient.getPhoneNumber());
        System.out.println("Patient data has been updated.");
        return updatedPatient;
    }

    @Transactional
    public Patient changePassword(String email, Patient updatedPatient) {
        Patient patientToChangePassword = patientRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Patient with the provided email does not exists"));
        if (patientToChangePassword.getPassword().equals(updatedPatient.getPassword())) {
            throw new IncorrectPasswordException("New password cannot be the same as the old password");
        }
        patientValidator.checkPatientPassword(updatedPatient);
        patientToChangePassword.setPassword(updatedPatient.getPassword());
        return patientToChangePassword;
    }
}
