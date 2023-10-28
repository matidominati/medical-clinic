package com.github.matidominati.medicalclinic.mapper;

import com.github.matidominati.medicalclinic.model.dto.InstitutionDto;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.model.entity.Institution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InstitutionMapper {

    @Mapping(source = "doctors", target = "doctorIds", qualifiedByName = "mapIds")
    InstitutionDto institutionToInstitutionDto(Institution institution);

    @Named("mapIds")
    default List<Long> mapIds(List<Doctor> doctors) {
        if (doctors == null) {
            return new ArrayList<>();
        }
        return doctors.stream()
                .map(doctor -> doctor.getId())
                .collect(Collectors.toList());
    }
}
