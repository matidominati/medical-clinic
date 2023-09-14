package com.github.matidominati.medicalclinic.personInfo;

import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Data
@RestController
@RequestMapping("/patients")
public class PatientController {
    private final List<Patient> patients;
    @GetMapping
    public List<Patient> getAllPatients() {
        return patients;
    }
    @GetMapping("/{email}")
    public Patient getPatient (@PathVariable String email) {
        return checkIfPatientExist(email);
    }
    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {
        if (!patients.contains(patient) && patient.getEmail() != null) {
            patients.add(patient);
            return patient;
        } else {
            throw new RuntimeException("Pacjent już istnieje w bazie");
        }
    }
    @DeleteMapping("/{email}")
    public void deletePatient(@PathVariable String email) {
        Patient patientToDelete = checkIfPatientExist(email);
        if (patientToDelete != null) {
            patients.remove(patientToDelete);
        } else {
            throw new RuntimeException("Pacjent o podanym email nie istnieje w bazie");
        }
    }
    @PutMapping("/{email}")
    public void updatePatient(@PathVariable String email, @RequestBody Patient updatedPatient) {
        Patient patientToUpdate = checkIfPatientExist(email);
        patientToUpdate.setPassword(updatedPatient.getPassword());
        patientToUpdate.setFirstName(patientToUpdate.getFirstName());
        patientToUpdate.setLastName(updatedPatient.getLastName());
        patientToUpdate.setPhoneNumber(updatedPatient.getPhoneNumber());
    }
    public Patient checkIfPatientExist (String email){
        Optional<Patient> findPatient = patients.stream()
                .filter(patient -> patient.getEmail() != null && patient.getEmail().equals(email))
                .findFirst();
        return findPatient.orElse(null);
    }
}
