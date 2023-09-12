package com.github.matidominati.medicalclinic.personInfo;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private List<Patient> patients = new ArrayList<>();

    @GetMapping
    public List<Patient> getAllPatients() {
        return patients;
    }

    @GetMapping("/{email}")
    public Patient getPerson(@PathVariable String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Pacjent nie istnieje w bazie"));

    }

    @PostMapping
    public void addPatient(@RequestBody Patient patient) {
        if (!patients.contains(patient)) {
            patients.add(patient);
        } else throw new RuntimeException("Pacjent już istnieje w bazie");
    }

    @DeleteMapping("/{email}")
    public void deletePatient(@PathVariable String email) {
        Patient patientToDelete = getPerson(email);
        if (patientToDelete != null) {
            patients.remove(patientToDelete);
        } else {
            throw new RuntimeException("Pacjent o podanym email nie istnieje w bazie");
        }
    }

    @PutMapping("/{email}")
    public void updatePatient(@PathVariable String email, @RequestBody Patient updatedPatient) {
        Patient patientToUpdate = getPerson(email);
        patientToUpdate.setPassword(updatedPatient.getPassword());
        patientToUpdate.setFirstName(patientToUpdate.getFirstName());
        patientToUpdate.setLastName(updatedPatient.getLastName());
        patientToUpdate.setPhoneNumber(updatedPatient.getPhoneNumber());


    }
}
