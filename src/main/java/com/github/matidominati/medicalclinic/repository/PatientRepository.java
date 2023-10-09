package com.github.matidominati.medicalclinic.repository;

import com.github.matidominati.medicalclinic.enity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByEmail(String email);

}