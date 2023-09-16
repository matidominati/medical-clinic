package com.github.matidominati.medicalclinic.repository;

import com.github.matidominati.medicalclinic.personInfo.Patient;

import java.util.List;
import java.util.Optional;


public interface PatientRepository {
    List<Patient> getAllPatients();
    Patient getPatient(String email);
    Optional<Patient> findPatientByEmail(String email);
    void deletePatientByEmail (String email);
}