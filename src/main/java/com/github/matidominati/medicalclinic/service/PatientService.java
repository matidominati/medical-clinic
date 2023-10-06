package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.*;
import com.github.matidominati.medicalclinic.model.Patient;
import com.github.matidominati.medicalclinic.repository.PatientRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientValidator patientValidator;
    private final PatientRepositoryImpl patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.getAllPatients();
    }

    public Patient getPatient(String email) {
        return patientRepository.findPatientByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Patient with the provided email does not exists"));
    }

    public Patient addPatient(Patient patient) {
        patientValidator.checkPatientData(patient);
        Optional<Patient> optionalPatient = patientRepository.findPatientByEmail(patient.getEmail());
        if(optionalPatient.isPresent()){
            throw new DataAlreadyExistsException("Patient with given email exists");
        }
        patientRepository.save(patient);
        return patient;
    }

    public void deletePatient(String email) {
        Optional<Patient> patientToDelete = patientRepository.findPatientByEmail(email);
        if (patientToDelete.isEmpty()) {
            throw new DataNotFoundException("The patient with the given email address does not exists in the database");
        }
        patientRepository.deletePatientByEmail(email);
    }

    public Patient updatePatient(String email, Patient updatedPatient) {
        patientValidator.checkPatientData(updatedPatient);
        Patient patient = patientRepository.findPatientByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Patient with the provided email does not exists"));
        if(!patient.getIdCardNo().equals(updatedPatient.getIdCardNo())){
            throw new ChangeIdException("Changing ID number is not allowed!");
        }
        patient.setPassword(updatedPatient.getPassword());
        patient.setFirstName(updatedPatient.getFirstName());
        patient.setLastName(updatedPatient.getLastName());
        patient.setPhoneNumber(updatedPatient.getPhoneNumber());
        System.out.println("Patient data has been updated.");
        return updatedPatient;
    }

    public Patient changePassword(String email, Patient updatedPatient) {
        Patient patientToChangePassword = patientRepository.findPatientByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Patient with the provided email does not exists"));
        if (patientToChangePassword.getPassword().equals(updatedPatient.getPassword())) {
            throw new IncorrectPasswordException("New password cannot be the same as the old password");
        }
        patientValidator.checkPatientPassword(updatedPatient);
        patientToChangePassword.setPassword(updatedPatient.getPassword());
        return patientToChangePassword;
    }

}
