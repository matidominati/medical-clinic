package com.github.matidominati.medicalclinic.service.mapper;
import com.github.matidominati.medicalclinic.service.dto.PatientDto;
import com.github.matidominati.medicalclinic.model.Patient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PatientMapper {
    public static PatientDto mapToDto(Patient patient) {
        PatientDto patientDto = new PatientDto();
        patientDto.setEmail(patient.getEmail());
        patientDto.setFirstName(patient.getFirstName());
        patientDto.setLastName(patient.getLastName());
        patientDto.setPhoneNumber(patient.getPhoneNumber());
        patientDto.setBirthDate(patient.getBirthDate());
        return patientDto;
    }
}
