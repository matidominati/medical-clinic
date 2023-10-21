package com.github.matidominati.medicalclinic.mapper;

import com.github.matidominati.medicalclinic.model.dto.InstitutionDto;
import com.github.matidominati.medicalclinic.model.entity.Institution;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InstitutionMapper {

    InstitutionDto InstitutionToInstitutionDto(Institution institution);
}
