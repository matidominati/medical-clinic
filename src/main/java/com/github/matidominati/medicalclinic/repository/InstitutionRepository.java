package com.github.matidominati.medicalclinic.repository;

import com.github.matidominati.medicalclinic.model.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    Optional<Institution> findByName(String email);
}
