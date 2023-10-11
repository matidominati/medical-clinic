package com.github.matidominati.medicalclinic.controller;

import com.github.matidominati.medicalclinic.service.dto.PatientDto;
import com.github.matidominati.medicalclinic.model.Patient;
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

    @GetMapping("/{email}")
    public PatientDto getPatient(@PathVariable String email) {
        return patientService.getPatient(email);
    }

    @PostMapping
    public PatientDto addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    @DeleteMapping("/{email}")
    public void deletePatient(@PathVariable String email) {
        patientService.deletePatient(email);
    }

    @PutMapping("/{email}")
    public PatientDto updatePatient(@PathVariable String email, @RequestBody Patient updatedPatient) {
       return patientService.updatePatient(email, updatedPatient);
    }

    @PatchMapping("/{email}")
    public PatientDto changePassword(@PathVariable String email, @RequestBody Patient updatedPatient) {
        return patientService.changePassword(email, updatedPatient);
    }
}
