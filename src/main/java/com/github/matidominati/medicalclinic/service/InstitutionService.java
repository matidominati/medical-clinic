package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.DataNotFoundException;
import com.github.matidominati.medicalclinic.mapper.InstitutionMapper;
import com.github.matidominati.medicalclinic.model.dto.InstitutionDto;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateInstitutionCommand;
import com.github.matidominati.medicalclinic.model.dto.commandDto.editCommand.EditInstitutionCommand;
import com.github.matidominati.medicalclinic.model.entity.Institution;
import com.github.matidominati.medicalclinic.repository.InstitutionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstitutionService {
    private final InstitutionRepository institutionRepository;
    private final InstitutionMapper institutionMapper;

    public List<InstitutionDto> getAllInstitutions() {
        return institutionRepository.findAll().stream()
                .map(institution -> institutionMapper.InstitutionToInstitutionDto(institution))
                .collect(Collectors.toList());
    }

    public InstitutionDto getInstitutionById(Long id) {
        Institution institution = institutionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Institution with the provided ID does not exist"));
        return institutionMapper.InstitutionToInstitutionDto(institution);
    }

    @Transactional
    public InstitutionDto addInstitution(CreateInstitutionCommand createInstitution) {
        Institution institution = Institution.create(createInstitution);
        institutionRepository.save(institution);
        return institutionMapper.InstitutionToInstitutionDto(institution);
    }

    @Transactional
    public void deleteInstitution(Long id) {
        Optional<Institution> institutionToDelete = institutionRepository.findById(id);
        if (institutionToDelete.isEmpty()) {
            throw new DataNotFoundException("The institution with the given ID does not exist in the database");
        }
        institutionRepository.delete(institutionToDelete.get());
    }

    @Transactional
    public InstitutionDto updateInstitution(Long id, EditInstitutionCommand updatedInstitution) {
        Institution institution = institutionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Institution with the provided ID does not exist"));
        institutionRepository.save(institution);
        institution.setName(updatedInstitution.getName());
        institution.setAddress(updatedInstitution.getAddress());
        institution.setDoctors(updatedInstitution.getDoctors());
        return  institutionMapper.InstitutionToInstitutionDto(institution);

    }
}
