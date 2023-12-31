package com.github.matidominati.medicalclinic.repository;

import com.github.matidominati.medicalclinic.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface PatientRepository extends JpaRepository<Patient, Long> {
}