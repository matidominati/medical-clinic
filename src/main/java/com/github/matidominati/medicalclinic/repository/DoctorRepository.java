package com.github.matidominati.medicalclinic.repository;

import com.github.matidominati.medicalclinic.model.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
