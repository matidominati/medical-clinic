package com.github.matidominati.medicalclinic.mapper;

import com.github.matidominati.medicalclinic.model.dto.DoctorDto;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DoctorMapper {
    DoctorDto doctorToDoctorDto(Doctor doctor);
}
