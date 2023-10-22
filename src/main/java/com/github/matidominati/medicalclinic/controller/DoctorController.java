package com.github.matidominati.medicalclinic.controller;

import com.github.matidominati.medicalclinic.model.dto.InstitutionDto;
import com.github.matidominati.medicalclinic.model.dto.VisitDto;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateDoctorCommand;
import com.github.matidominati.medicalclinic.model.dto.DoctorDto;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateVisitCommand;
import com.github.matidominati.medicalclinic.model.dto.commandDto.editCommand.EditDoctorCommand;
import com.github.matidominati.medicalclinic.service.DoctorService;
import com.github.matidominati.medicalclinic.service.InstitutionService;
import com.github.matidominati.medicalclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final VisitService visitService;
    private final InstitutionService institutionService;

    @GetMapping
    public List<DoctorDto> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}")
    public DoctorDto getDoctor(@PathVariable Long id) {
        return doctorService.getDoctor(id);
    }

    @PostMapping
    public DoctorDto addDoctor(@RequestBody CreateDoctorCommand createDoctor) {
        return doctorService.addDoctor(createDoctor);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }

    @PutMapping("/{id}")
    public DoctorDto updateDoctor(@PathVariable Long id, @RequestBody EditDoctorCommand updatedDoctor) {
        return doctorService.updateDoctor(id, updatedDoctor);
    }

    @PatchMapping("/{doctorId}/institutions/{institutionId}/visits/create")
    public VisitDto createVisit(@PathVariable Long doctorId, @PathVariable Long institutionId, @RequestBody CreateVisitCommand createVisit) {
        return visitService.addVisit(createVisit, doctorId, institutionId);
    }

    @PatchMapping("/{doctorId}/institutions/add/{institutionId}")
    public DoctorDto addToInstitution(@PathVariable Long doctorId, @PathVariable Long institutionId) {
        return doctorService.addToInstitution(doctorId, institutionId);
    }
}
