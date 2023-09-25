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
    public Patient save(Patient patient) {
        patients.add(patient);
        return patient;
    }

    @Override
    public Optional<Patient> findPatientByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail() != null && patient.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<Patient> deletePatientByEmail(String email) {
        Optional<Patient> patientToDelete = findPatientByEmail(email);
        if (patientToDelete.isEmpty()) {
            Optional.empty();
        }
        patients.remove(patientToDelete.get());
        return patientToDelete;
    }

}
