package com.github.matidominati.medicalclinic.controller;

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
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{email}")
    public Patient getPatient(@PathVariable String email) {
        return patientService.getPatient(email);
    }

    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    @DeleteMapping("/{email}")
    public void deletePatient(@PathVariable String email) {
        patientService.deletePatient(email);
    }

    @PutMapping("/{email}")
    public Patient updatePatient(@PathVariable String email, @RequestBody Patient updatedPatient) {
       return patientService.updatePatient(email, updatedPatient);
    }
    @PatchMapping("/{email}")
    public Patient changePassword(@PathVariable String email, @RequestBody Patient updatedPatient) {
        return patientService.changePassword(email, updatedPatient);
    }
}
