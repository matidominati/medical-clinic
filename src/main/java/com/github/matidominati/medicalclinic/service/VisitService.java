package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.DataNotFoundException;
import com.github.matidominati.medicalclinic.mapper.VisitMapper;
import com.github.matidominati.medicalclinic.model.dto.VisitDto;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateVisitCommand;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.model.entity.Institution;
import com.github.matidominati.medicalclinic.model.entity.Patient;
import com.github.matidominati.medicalclinic.model.entity.Visit;
import com.github.matidominati.medicalclinic.model.enums.VisitType;
import com.github.matidominati.medicalclinic.repository.DoctorRepository;
import com.github.matidominati.medicalclinic.repository.InstitutionRepository;
import com.github.matidominati.medicalclinic.repository.PatientRepository;
import com.github.matidominati.medicalclinic.repository.VisitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.matidominati.medicalclinic.service.validator.CRUDDataValidator.findByIdOrThrow;
import static com.github.matidominati.medicalclinic.service.validator.VisitDataValidator.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VisitService {
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;
    private final DoctorRepository doctorRepository;
    private final InstitutionRepository institutionRepository;
    private final PatientRepository patientRepository;

    public List<VisitDto> getAllVisits() {
        log.info("Search process for all visits has started");
        List<VisitDto> visits = visitRepository.findAll().stream()
                .map(visitMapper::visitToVisitDto)
                .toList();
        log.info("{} visits found", visits.size());
        return visits;
    }

    public VisitDto getVisitById(Long visitId) {
        log.info("Process of searching for a visit with ID: {} has started", visitId);
        try {
            Visit visit = findByIdOrThrow(visitId, visitRepository, Visit.class);
            log.info("Found visits with ID: {}", visitId);
            return visitMapper.visitToVisitDto(visit);
        } catch (DataNotFoundException e) {
            log.warn("Visit with ID: {} not found", visitId);
            throw e;
        }
    }

    @Transactional
    public VisitDto createVisit(CreateVisitCommand createVisit, Long doctorId, Long institutionId) {
        log.info("Process of searching for a doctor with ID: {} has started", doctorId);
        Doctor doctor = findByIdOrThrow(doctorId, doctorRepository, Doctor.class);
        log.info("Process of searching for an institution with ID: {} has started", institutionId);
        Institution institution = findByIdOrThrow(institutionId, institutionRepository, Institution.class);
        if (!doctor.getInstitutions().contains(institution)) {
            throw new DataNotFoundException("Doctor with the provided ID does not cooperate with this institution");
        }
        log.info("Checking if doctor with ID: {} has an available appointment", doctorId);
        checkIfDoctorCanCreateVisit(doctor, createVisit);
        log.info("Checking if visit date is correct. {}", createVisit);
        checkIfVisitTimeIsCorrect(createVisit.getStartDateTime(), createVisit.getEndDateTime());
        Visit visit = Visit.createVisit(createVisit);
        visit.setDoctor(doctor);
        visit.setInstitution(institution);
        visitRepository.save(visit);
        log.info("The visit has been added. {}", createVisit);
        return visitMapper.visitToVisitDto(visit);
    }

    @Transactional
    public VisitDto bookVisit(Long patientId, Long visitId) {
        log.info("Process of searching for a patient with ID: {} has started", patientId);
        Patient patientToBookVisit = findByIdOrThrow(patientId, patientRepository, Patient.class);
        log.info("Checking if visit with ID {} is available.", visitId);
        Visit visitToBook = checkIfVisitIsAvailable(visitId, visitRepository);
        log.info("Checking if patient with ID {} can book a visit", patientId);
        checkIfPatientCanBookVisit(visitToBook, patientToBookVisit);
        patientToBookVisit.getVisits().add(visitToBook);
        visitToBook.setStatus(VisitType.SCHEDULED);
        visitToBook.setPatient(patientToBookVisit);
        visitRepository.save(visitToBook);
        patientRepository.save(patientToBookVisit);
        log.info("Visit was booked correctly for patient with ID {}", patientId);
        return visitMapper.visitToVisitDto(visitToBook);
    }

    public List<VisitDto> getAllPatientVisits(Long patientId) {
        log.info("Process of searching for a patient with ID: {} has started", patientId);
        try {
            Patient patient = findByIdOrThrow(patientId, patientRepository, Patient.class);
            log.info("Found patient with ID: {}", patientId);
            List<VisitDto> patientVisits = patient.getVisits().stream()
                    .map(visitMapper::visitToVisitDto)
                    .toList();
            log.info("{} patient visits found", patientVisits.size());
            return patientVisits;
        } catch (DataNotFoundException e) {
            log.warn("Patient with ID: {} not found", patientId);
            throw e;
        }
    }
}