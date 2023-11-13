package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.DataNotFoundException;
import com.github.matidominati.medicalclinic.mapper.InstitutionMapper;
import com.github.matidominati.medicalclinic.model.dto.InstitutionDto;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateInstitutionCommand;
import com.github.matidominati.medicalclinic.model.dto.commandDto.editCommand.EditInstitutionCommand;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.model.entity.Institution;
import com.github.matidominati.medicalclinic.repository.DoctorRepository;
import com.github.matidominati.medicalclinic.repository.InstitutionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.matidominati.medicalclinic.service.validator.CRUDDataValidator.findByIdOrThrow;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstitutionService {
    private final InstitutionRepository institutionRepository;
    private final InstitutionMapper institutionMapper;
    private final DoctorRepository doctorRepository;

    public List<InstitutionDto> getAllInstitutions() {
        log.info("Search process for all institutions has started");
        List<InstitutionDto> institutions = institutionRepository.findAll().stream()
                .map(institutionMapper::institutionToInstitutionDto)
                .toList();
        log.info("{} institutions found", institutions.size());
        return institutions;
    }

    public InstitutionDto getInstitutionById(Long id) {
        log.info("Process of searching for a institution with ID: {} has started", id);
        try {
            Institution institution = findByIdOrThrow(id, institutionRepository, Institution.class);
            return institutionMapper.institutionToInstitutionDto(institution);
        } catch (DataNotFoundException e) {
            log.info("Institution with ID: {} not found", id);
            throw e;
        }
    }

    @Transactional
    public InstitutionDto addInstitution(CreateInstitutionCommand createInstitution) {
        log.info("Process of creating institution has started. {}", createInstitution);
        Institution institution = Institution.createInstitution(createInstitution);
        institutionRepository.save(institution);
        log.info("New institution with ID: {} has been created.", institution.getId());
        return institutionMapper.institutionToInstitutionDto(institution);
    }

    @Transactional
    public void deleteInstitution(Long id) {
        log.info("Process of searching for a institution with ID: {} has started", id);
        try {
            Institution institutionToDelete = findByIdOrThrow(id, institutionRepository, Institution.class);
            institutionRepository.delete(institutionToDelete);
            log.info("Institution with ID: {} has been removed", id);
        } catch (DataNotFoundException e) {
            log.info("Institution with ID: {} not found", id);
            throw e;
        }
    }

    @Transactional
    public InstitutionDto updateInstitution(Long id, EditInstitutionCommand updatedInstitution) {
        log.info("Process of searching for an institution with ID: {} has started", id);
        try {
            Institution institution = findByIdOrThrow(id, institutionRepository, Institution.class);
            institutionRepository.save(institution);
            institution.setName(updatedInstitution.getName());
            institution.setAddress(updatedInstitution.getAddress());
            institution.setDoctors(updatedInstitution.getDoctors());
            log.debug("Institution data has been updated. {}", updatedInstitution);
            return institutionMapper.institutionToInstitutionDto(institution);
        } catch (DataNotFoundException e) {
            log.info("Institution with ID: {} not found", id);
            throw e;
        }
    }

    @Transactional
    public InstitutionDto addDoctor(Long doctorId, Long institutionId) {
        log.info("Process of searching for a doctor with ID: {} has started", doctorId);
        Doctor doctorToAdd = findByIdOrThrow(doctorId, doctorRepository, Doctor.class);
        log.info("Process of searching for an institution with ID: {} has started", institutionId);
        Institution institution = findByIdOrThrow(institutionId, institutionRepository, Institution.class);
        doctorToAdd.getInstitutions().add(institution);
        doctorRepository.save(doctorToAdd);
        institutionRepository.saveAndFlush(institution);
        log.info("Doctor with ID: {} has been added to the institution with ID: {}.", doctorId, institutionId);
        return institutionMapper.institutionToInstitutionDto(institution);
    }
}