package com.github.matidominati.medicalclinic.mapper;

import com.github.matidominati.medicalclinic.model.dto.VisitDto;
import com.github.matidominati.medicalclinic.model.entity.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VisitMapper {
    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "institution.name", target = "institutionName")
    @Mapping(source = "institution.address", target = "institutionAddress")
    VisitDto visitToVisitDto(Visit visit);
}

