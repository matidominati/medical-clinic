package com.github.matidominati.medicalclinic.repository;

import com.github.matidominati.medicalclinic.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientRepositoryImpl implements PatientRepository {
    private final List<Patient> patients = new ArrayList<>();

    @Override
    public List<Patient> getAllPatients() {
        return patients;
    }

    @Override
    public Patient getPatient(String email) {
        Optional<Patient> patient = findPatientByEmail(email);
        if (patient.isEmpty()) {
            throw new RuntimeException("Patient with the provided email does not exist");
        }
        return patient.get();
    }

    @Override
    public Optional<Patient> findPatientByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail() != null && patient.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public void deletePatientByEmail(String email) {
        Optional<Patient> patientToDelete = findPatientByEmail(email);
        if (patientToDelete.isEmpty()) {
            throw new IllegalArgumentException("The patient with the given email address does not exist in the database");
        }
        patients.remove(patientToDelete.get());
    }

}
