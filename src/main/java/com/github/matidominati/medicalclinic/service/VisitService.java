package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.DataNotFoundException;
import com.github.matidominati.medicalclinic.mapper.VisitMapper;
import com.github.matidominati.medicalclinic.model.dto.VisitDto;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateVisitCommand;
import com.github.matidominati.medicalclinic.model.entity.*;
import com.github.matidominati.medicalclinic.model.enums.VisitType;
import com.github.matidominati.medicalclinic.repository.DoctorRepository;
import com.github.matidominati.medicalclinic.repository.InstitutionRepository;
import com.github.matidominati.medicalclinic.repository.PatientRepository;
import com.github.matidominati.medicalclinic.repository.VisitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;
    private final DoctorRepository doctorRepository;
    private final InstitutionRepository institutionRepository;
    private final PatientRepository patientRepository;

    public List<VisitDto> getAllVisits() {
        return visitRepository.findAll().stream()
                .map(visit -> visitMapper.visitToVisitDto(visit))
                .collect(Collectors.toList());
    }


    public VisitDto getVisitsById(Long id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Visit with provided ID does not exist"));
        return visitMapper.visitToVisitDto(visit);
    }

    @Transactional
    public VisitDto addVisit(CreateVisitCommand createVisit, Long doctorId, Long institutionId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(()-> new DataNotFoundException("Doctor with the provided ID does not exist"));
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new DataNotFoundException("Institution with the provided ID does not exist"));
        if (!doctor.getInstitutions().contains(institution)){
            throw new DataNotFoundException("Doctor with the provided ID does not cooperate with this institution");
        }
        Visit visit = Visit.create(createVisit);
        visit.setDoctor(doctor);
        visit.setInstitution(institution);
        visitRepository.save(visit);
        return visitMapper.visitToVisitDto(visit);
    }
    @Transactional
    public VisitDto bookVisit(Long patientId, Long visitId) {
        Patient patientToBookVisit = patientRepository.findById(patientId)
                .orElseThrow(() -> new DataNotFoundException("Patient with the provided ID does not exist"));

        Visit visitToBook = visitRepository.findById(visitId)
                .orElseThrow(() -> new DataNotFoundException("Visit with the provided ID does not exist"));

        if (visitToBook.getStatus() != VisitType.CREATED) {
            throw new DataNotFoundException("Visit is unavailable");
        }
        patientToBookVisit.getVisits().add(visitToBook);
        visitToBook.setStatus(VisitType.SCHEDULED);
        visitToBook.setPatient(patientToBookVisit);
        visitRepository.save(visitToBook);
        patientRepository.save(patientToBookVisit);
        return visitMapper.visitToVisitDto(visitToBook);
    }
}
