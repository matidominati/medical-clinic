package com.github.matidominati.medicalclinic.personInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
            throw new RuntimeException("Patient with the provided email does not exist");
        }
        return patient.get();
    }

    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {

        checkPatientData(patient);
        if (patients.contains(patient)) {
            throw new IllegalArgumentException("The patient already exists in the database");
        }
        patients.add(patient);
        return patient;
    }

    @DeleteMapping("/{email}")
    public void deletePatient(@PathVariable String email) {
        Optional<Patient> patientToDelete = findPatientByEmail(email);
        if (patientToDelete.isEmpty()) {
            throw new IllegalArgumentException("The patient with the given email address does not exist in the database");
        }
        patients.remove(patientToDelete.get());
    }

    @PutMapping("/{email}")
    public void updatePatient(@PathVariable String email, @RequestBody Patient updatedPatient) {
        Optional<Patient> patientOptional = findPatientByEmail(email);
        if (patientOptional.isEmpty()) {
            throw new IllegalArgumentException("The patient with the given email address does not exist in the database");
        }
        patientOptional.ifPresent(patientToUpdate -> {
            checkPatientEditableData(updatedPatient);
            patientToUpdate.setPassword(updatedPatient.getPassword());
            patientToUpdate.setFirstName(patientToUpdate.getFirstName());
            patientToUpdate.setLastName(updatedPatient.getLastName());
            patientToUpdate.setPhoneNumber(updatedPatient.getPhoneNumber());
        });
        System.out.println("Patient data has been updated.");
    }

    @PatchMapping("/{email}")
    public void changePassword(@PathVariable String email, @RequestBody Patient updatedPatient) {
        Optional<Patient> patientToChangePassword = findPatientByEmail(email);
        if (patientToChangePassword.isEmpty()) {
            throw new RuntimeException("Patient with the provided email does not exist");
        }
        if (patientToChangePassword.get().getPassword().equals(updatedPatient.getPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as the old password");
        }
        checkPatientPassword(updatedPatient);
        System.out.println("Password has been changed.");
    }

    private Optional<Patient> findPatientByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail() != null && patient.getEmail().equals(email))
                .findFirst();
    }

    private void checkPatientEditableData(Patient patient) {
        if (patient.getFirstName() == null) {
            throw new IllegalArgumentException("First name cannot be null");
        }
        if (patient.getLastName() == null) {
            throw new IllegalArgumentException("Last name cannot be null");
        }
        if (patient.getPhoneNumber() == null || patient.getPhoneNumber().length() < 9) {
            throw new IllegalArgumentException("Phone number must consist of nine digits");
        }
        checkPatientPassword(patient);
    }

    private void checkPatientUneditableData(Patient patient) {
        if (patient.getEmail() == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (patient.getIdCardNo() == null) {
            throw new IllegalArgumentException("Card ID number cannot be null");
        }
        if (patient.getBirthday() == null || patient.getBirthday().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birthday date is incorrect");
        }
    }

    private void checkPatientData(Patient patient) {
        checkPatientEditableData(patient);
        checkPatientUneditableData(patient);
    }

    private void checkPatientPassword(Patient patient) {
        if (patient.getPassword() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        if (patient.getPassword().equals(patient.getFirstName()) || patient.getPassword().equals(patient.getLastName())) {
            throw new IllegalArgumentException("Password cannot be the same as the first name or last name");
        }
        if (patient.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must consist of more than six characters.");
        }
    }
}
