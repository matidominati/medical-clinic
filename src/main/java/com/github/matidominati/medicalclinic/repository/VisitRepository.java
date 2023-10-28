package com.github.matidominati.medicalclinic.repository;

import com.github.matidominati.medicalclinic.model.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {
}
