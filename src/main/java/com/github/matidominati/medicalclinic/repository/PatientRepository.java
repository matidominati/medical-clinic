package com.github.matidominati.medicalclinic.repository;

import com.github.matidominati.medicalclinic.model.Patient;

import java.util.List;
import java.util.Optional;


public interface PatientRepository {
    List<Patient> getAllPatients();
    Optional<Patient> findPatientByEmail(String email);
    void deletePatientByEmail (String email);
}