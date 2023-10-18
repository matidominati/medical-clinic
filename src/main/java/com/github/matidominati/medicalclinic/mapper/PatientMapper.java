package com.github.matidominati.medicalclinic.mapper;

import com.github.matidominati.medicalclinic.model.dto.PatientDto;
import com.github.matidominati.medicalclinic.model.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientMapper {

    PatientDto patientToPatientDto (Patient patient);
    Patient patientDtoToPatient (PatientDto PatientDto);
}
