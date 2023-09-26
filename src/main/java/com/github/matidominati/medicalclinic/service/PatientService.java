package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.ApiRequestException;
import com.github.matidominati.medicalclinic.model.Patient;
import com.github.matidominati.medicalclinic.repository.PatientRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientValidator patientValidator;
    private final PatientRepositoryImpl patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.getAllPatients();
    }

    public Patient getPatient(String email) {
        return patientRepository.findPatientByEmail(email)
                .orElseThrow(() -> new ApiRequestException("Patient with the provided email does not exists", HttpStatus.NOT_FOUND));
    }

    public Patient addPatient(Patient patient) {
        patientValidator.checkPatientData(patient);
        Optional<Patient> optionalPatient = patientRepository.findPatientByEmail(patient.getEmail());
        if(optionalPatient.isPresent()){
            throw new ApiRequestException("Patient with given email exists", HttpStatus.BAD_REQUEST);
        }
        patientRepository.save(patient);
        return patient;
    }

    public void deletePatient(String email) {
        Optional<Patient> patientToDelete = patientRepository.findPatientByEmail(email);
        if (patientToDelete.isEmpty()) {
            throw new ApiRequestException("The patient with the given email address does not exists in the database", HttpStatus.NOT_FOUND);
        }
        patientRepository.deletePatientByEmail(email);
    }

    public void updatePatient(String email, Patient updatedPatient) {
        patientValidator.checkPatientEditableData(updatedPatient);
        Patient patient = patientRepository.findPatientByEmail(email)
                .orElseThrow(() -> new ApiRequestException("Patient with the provided email does not exists", HttpStatus.NOT_FOUND));
        patient.setPassword(updatedPatient.getPassword());
        patient.setFirstName(updatedPatient.getFirstName());
        patient.setLastName(updatedPatient.getLastName());
        patient.setPhoneNumber(updatedPatient.getPhoneNumber());
        System.out.println("Patient data has been updated.");
    }

    public void changePassword(String email, Patient updatedPatient) {
        Patient patientToChangePassword = patientRepository.findPatientByEmail(email)
                .orElseThrow(() -> new ApiRequestException("Patient with the provided email does not exists", HttpStatus.NOT_FOUND));
        if (patientToChangePassword.getPassword().equals(updatedPatient.getPassword())) {
            throw new ApiRequestException("New password cannot be the same as the old password", HttpStatus.BAD_REQUEST);
        }
        patientValidator.checkPatientPassword(updatedPatient);
        System.out.println("Password has been changed.");
    }

}
