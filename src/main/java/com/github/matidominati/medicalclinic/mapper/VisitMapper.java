package com.github.matidominati.medicalclinic.mapper;

import com.github.matidominati.medicalclinic.model.dto.VisitDto;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.model.entity.Institution;
import com.github.matidominati.medicalclinic.model.entity.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VisitMapper {
    @Mapping(source = "doctor", target = "doctorId", qualifiedByName = "mapDoctorId")
    @Mapping(source = "institution", target = "institutionName", qualifiedByName = "mapInstitutionName")
    @Mapping(source = "institution", target = "institutionAddress", qualifiedByName = "mapInstitutionAddress")
    VisitDto visitToVisitDto(Visit visit);

    @Named("mapDoctorId")
    default Long mapDoctorId(Doctor doctor) {
        return doctor.getId();
    }
    @Named("mapInstitutionName")
    default String mapInstitutionName(Institution institution) {
        return institution.getName();
    }
    @Named("mapInstitutionAddress")
    default String mapInstitutionAddress(Institution institution) {
        return institution.getAddress();
    }
}
