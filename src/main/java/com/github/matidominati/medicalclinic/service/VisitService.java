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
import com.github.matidominati.medicalclinic.service.validator.VisitDataValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.matidominati.medicalclinic.service.validator.CRUDDataValidator.findByIdOrThrow;

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

    public VisitDto getVisitsById(Long visitId) {
        Visit visit = findByIdOrThrow(visitId, visitRepository, "Visit");
        return visitMapper.visitToVisitDto(visit);
    }

    @Transactional
    public VisitDto createVisit(CreateVisitCommand createVisit, Long doctorId, Long institutionId) {
        Doctor doctor = findByIdOrThrow(doctorId, doctorRepository, "Doctor");
        Institution institution = findByIdOrThrow(institutionId, institutionRepository, "Institution");
        if (!doctor.getInstitutions().contains(institution)){
            throw new DataNotFoundException("Doctor with the provided ID does not cooperate with this institution");
        }
        VisitDataValidator.checkIfDoctorCanCreateVisit(doctor, createVisit);
        VisitDataValidator.checkIfVisitTimeIsCorrect(createVisit.getStartDateTime(), createVisit.getEndDateTime());
        Visit visit = Visit.create(createVisit);
        visit.setDoctor(doctor);
        visit.setInstitution(institution);
        visitRepository.save(visit);
        return visitMapper.visitToVisitDto(visit);
    }

    @Transactional
    public VisitDto bookVisit(Long patientId, Long visitId) {
        Patient patientToBookVisit = findByIdOrThrow(patientId, patientRepository, "Patient");
        Visit visitToBook = VisitDataValidator.checkIfVisitIsAvailable(visitId, visitRepository);
        VisitDataValidator.checkIfPatientCanBookVisit(visitToBook, patientToBookVisit);
        patientToBookVisit.getVisits().add(visitToBook);
        visitToBook.setStatus(VisitType.SCHEDULED);
        visitToBook.setPatient(patientToBookVisit);
        visitRepository.save(visitToBook);
        patientRepository.save(patientToBookVisit);
        return visitMapper.visitToVisitDto(visitToBook);
    }

    public List<VisitDto> getAllPatientVisits(Long patientId) {
        Patient patient = findByIdOrThrow(patientId, patientRepository, "Patient");
        return patient.getVisits().stream()
                .map(visitMapper::visitToVisitDto)
                .collect(Collectors.toList());
    }
}