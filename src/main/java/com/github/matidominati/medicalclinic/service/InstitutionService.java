package com.github.matidominati.medicalclinic.service;

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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.matidominati.medicalclinic.service.validator.CRUDDataValidator.findByIdOrThrow;

@Service
@RequiredArgsConstructor
public class InstitutionService {
    private final InstitutionRepository institutionRepository;
    private final InstitutionMapper institutionMapper;
    private final DoctorRepository doctorRepository;

    public List<InstitutionDto> getAllInstitutions() {
        return institutionRepository.findAll().stream()
                .map(institutionMapper::institutionToInstitutionDto)
                .collect(Collectors.toList());
    }

    public InstitutionDto getInstitutionById(Long id) {
        Institution institution = findByIdOrThrow(id, institutionRepository, "Institution");
        return institutionMapper.institutionToInstitutionDto(institution);
    }

    @Transactional
    public InstitutionDto addInstitution(CreateInstitutionCommand createInstitution) {
        Institution institution = Institution.create(createInstitution);
        institutionRepository.save(institution);
        return institutionMapper.institutionToInstitutionDto(institution);
    }

    @Transactional
    public void deleteInstitution(Long id) {
        Institution institutionToDelete = findByIdOrThrow(id, institutionRepository, "Institution");
        institutionRepository.delete(institutionToDelete);
    }

    @Transactional
    public InstitutionDto updateInstitution(Long id, EditInstitutionCommand updatedInstitution) {
        Institution institution = findByIdOrThrow(id, institutionRepository, "Institution");
        institutionRepository.save(institution);
        institution.setName(updatedInstitution.getName());
        institution.setAddress(updatedInstitution.getAddress());
        institution.setDoctors(updatedInstitution.getDoctors());
        return institutionMapper.institutionToInstitutionDto(institution);
    }

    @Transactional
    public InstitutionDto addDoctor(Long doctorId, Long institutionId) {
        Doctor doctorToAdd = findByIdOrThrow(doctorId, doctorRepository, "Doctor");
        Institution institution = findByIdOrThrow(institutionId, institutionRepository, "Institution");
        doctorToAdd.getInstitutions().add(institution);
        doctorRepository.save(doctorToAdd);
        institutionRepository.saveAndFlush(institution);
        return institutionMapper.institutionToInstitutionDto(institution);
    }
}