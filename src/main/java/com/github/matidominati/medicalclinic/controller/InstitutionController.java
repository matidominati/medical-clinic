package com.github.matidominati.medicalclinic.controller;

import com.github.matidominati.medicalclinic.model.dto.InstitutionDto;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateInstitutionCommand;
import com.github.matidominati.medicalclinic.model.dto.commandDto.editCommand.EditInstitutionCommand;
import com.github.matidominati.medicalclinic.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/institutions")
public class InstitutionController {
    private final InstitutionService institutionService;

    @GetMapping
    public List<InstitutionDto> getAllInstitutions() {
        return institutionService.getAllInstitutions();
    }

    @GetMapping("/{id}")
    public InstitutionDto getInstitution(@PathVariable Long id) {
        return institutionService.getInstitutionById(id);
    }

    @PostMapping
    public InstitutionDto addInstitution(@RequestBody CreateInstitutionCommand createInstitution) {
        return institutionService.addInstitution(createInstitution);
    }

    @DeleteMapping("/{id}")
    public void deleteInstitution(@PathVariable Long id) {
        institutionService.deleteInstitution(id);
    }

    @PutMapping("/{id}")
    public InstitutionDto updateInstitution(@PathVariable Long id, @RequestBody EditInstitutionCommand editInstitution) {
      return institutionService.updateInstitution(id, editInstitution);
    }

    @PatchMapping("/{institutionId}/add-doctor/{doctorId}")
    public InstitutionDto addDoctor(@PathVariable Long doctorId, @PathVariable Long institutionId) {
        return institutionService.addDoctor(doctorId, institutionId);
    }
}
