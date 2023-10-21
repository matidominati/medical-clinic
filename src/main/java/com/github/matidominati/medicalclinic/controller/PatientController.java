package com.github.matidominati.medicalclinic.controller;

import com.github.matidominati.medicalclinic.model.dto.commandDto.CreatePatientCommand;
import com.github.matidominati.medicalclinic.model.dto.commandDto.EditPatientCommand;
import com.github.matidominati.medicalclinic.model.dto.PatientDto;
import com.github.matidominati.medicalclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public List<PatientDto> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public PatientDto getPatient(@PathVariable Long id) {
        return patientService.getPatient(id);
    }

    @PostMapping
    public PatientDto addPatient(@RequestBody CreatePatientCommand createPatient) {
        return patientService.addPatient(createPatient);
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }

    @PutMapping("/{id}")
    public PatientDto updatePatient(@PathVariable Long id, @RequestBody EditPatientCommand updatedPatient) {
       return patientService.updatePatient(id, updatedPatient);
    }

    @PatchMapping("/{id}")
    public PatientDto changePassword(@PathVariable Long id, @RequestBody EditPatientCommand updatedPatient) {
        return patientService.changePassword(id, updatedPatient);
    }
}
