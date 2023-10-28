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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.matidominati.medicalclinic.service.validator.CRUDDataValidator.findByIdOrThrow;
import static com.github.matidominati.medicalclinic.service.validator.VisitDataValidator.*;

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
                .map(visitMapper::visitToVisitDto)
                .collect(Collectors.toList());
    }

    public VisitDto getVisitById(Long visitId) {
        Visit visit = findByIdOrThrow(visitId, visitRepository, Visit.class);
        return visitMapper.visitToVisitDto(visit);
    }

    @Transactional
    public VisitDto createVisit(CreateVisitCommand createVisit, Long doctorId, Long institutionId) {
        Doctor doctor = findByIdOrThrow(doctorId, doctorRepository, Doctor.class);
        Institution institution = findByIdOrThrow(institutionId, institutionRepository, Institution.class);
        if (!doctor.getInstitutions().contains(institution)){
            throw new DataNotFoundException("Doctor with the provided ID does not cooperate with this institution");
        }
        checkIfDoctorCanCreateVisit(doctor, createVisit);
        checkIfVisitTimeIsCorrect(createVisit.getStartDateTime(), createVisit.getEndDateTime());
        Visit visit = Visit.createVisit(createVisit);
        visit.setDoctor(doctor);
        visit.setInstitution(institution);
        visitRepository.save(visit);
        return visitMapper.visitToVisitDto(visit);
    }

    @Transactional
    public VisitDto bookVisit(Long patientId, Long visitId) {
        Patient patientToBookVisit = findByIdOrThrow(patientId, patientRepository, Patient.class);
        Visit visitToBook = checkIfVisitIsAvailable(visitId, visitRepository);
        checkIfPatientCanBookVisit(visitToBook, patientToBookVisit);
        patientToBookVisit.getVisits().add(visitToBook);
        visitToBook.setStatus(VisitType.SCHEDULED);
        visitToBook.setPatient(patientToBookVisit);
        visitRepository.save(visitToBook);
        patientRepository.save(patientToBookVisit);
        return visitMapper.visitToVisitDto(visitToBook);
    }

    public List<VisitDto> getAllPatientVisits(Long patientId) {
        Patient patient = findByIdOrThrow(patientId, patientRepository, Patient.class);
        return patient.getVisits().stream()
                .map(visitMapper::visitToVisitDto)
                .collect(Collectors.toList());
    }
}