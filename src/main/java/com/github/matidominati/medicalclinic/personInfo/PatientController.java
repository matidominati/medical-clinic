package com.github.matidominati.medicalclinic.personInfo;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/patients")

public class PatientController {
    private final List<Patient> patients;

    @GetMapping
    public List<Patient> getAllPatients() {
        return patients;
    }

    @GetMapping("/{email}")
    public Patient getPatient(@PathVariable String email) {
        Optional<Patient> patient = findPatientByEmail(email);
        if (patient.isEmpty()) {
            throw new RuntimeException("Patient with the provied email does not exist");
        }
        return patient.get();
    }

    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {
        if (patient.getEmail() == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (patients.contains(patient)) {
            throw new RuntimeException("The patient already exists in the database");
        }
        patients.add(patient);
        return patient;
    }

    @DeleteMapping("/{email}")
    public void deletePatient(@PathVariable String email) {
        Optional<Patient> patientToDelete = findPatientByEmail(email);
        if (patientToDelete.isPresent()) {
            patients.remove(patientToDelete.get());
        } else {
            throw new RuntimeException("The patient with the given email address does not exist in the database");
        }
    }

    @PutMapping("/{email}")
    public void updatePatient(@PathVariable String email, @RequestBody Patient updatedPatient) {
        Optional<Patient> patientOptional = findPatientByEmail(email);
        patientOptional.ifPresent(patientToUpdate -> {
            patientToUpdate.setPassword(updatedPatient.getPassword());
            patientToUpdate.setFirstName(patientToUpdate.getFirstName());
            patientToUpdate.setLastName(updatedPatient.getLastName());
            patientToUpdate.setPhoneNumber(updatedPatient.getPhoneNumber());
        });
    }

    private Optional<Patient> findPatientByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail() != null && patient.getEmail().equals(email))
                .findFirst();
    }

}
