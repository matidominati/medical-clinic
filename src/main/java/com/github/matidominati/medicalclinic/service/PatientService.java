package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.model.Patient;
import com.github.matidominati.medicalclinic.repository.PatientRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final List<Patient> patients;
    private final PatientValidator patientValidator;
    private final PatientRepositoryImpl patientRepository;

    public List<Patient> getAllPatients() {
        return patients;
    }

    public Patient getPatient(String email) {
        return patientRepository.findPatientByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient with the provided email does not exist"));
    }

    public Patient addPatient(Patient patient) {
        patientValidator.checkPatientData(patient);
        if (patients.contains(patient)) {
            throw new IllegalArgumentException("The patient already exists in the database");
        }
        patients.add(patient);
        return patient;
    }

    public void deletePatient(String email) {
        Optional<Patient> patientToDelete = patientRepository.findPatientByEmail(email);
        if (patientToDelete.isEmpty()) {
            throw new IllegalArgumentException("The patient with the given email address does not exist in the database");
        }
        patients.remove(patientToDelete.get());
    }

    public void updatePatient(String email, Patient updatedPatient) {
        patientValidator.checkPatientEditableData(updatedPatient);
        Patient patient = patientRepository.findPatientByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient with the provided email does not exist"));

        patient.setPassword(updatedPatient.getPassword());
        patient.setFirstName(updatedPatient.getFirstName());
        patient.setLastName(updatedPatient.getLastName());
        patient.setPhoneNumber(updatedPatient.getPhoneNumber());
        System.out.println("Patient data has been updated.");
    }

    public void changePassword(String email, Patient updatedPatient) {

        Patient patientToChangePassword = patientRepository.findPatientByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient with the provided email does not exist"));

        if (patientToChangePassword.getPassword().equals(updatedPatient.getPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as the old password");
        }
        patientValidator.checkPatientPassword(updatedPatient);
        System.out.println("Password has been changed.");
    }

}
