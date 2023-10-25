package com.github.matidominati.medicalclinic.mapper;

import com.github.matidominati.medicalclinic.model.dto.VisitDto;
import com.github.matidominati.medicalclinic.model.entity.Visit;

public class VisitMapperImpl implements VisitMapper{

    @Override
    public VisitDto visitToVisitDto(Visit visit) {
        if ( visit == null ) {
            return null;
        }

        VisitDto visitDto = new VisitDto();

        visitDto.setDoctorId(visit.getDoctor().getId());
        visitDto.setInstitutionName(visit.getInstitution().getName());
        visitDto.setInstitutionAddress(visit.getInstitution().getAddress());
        visitDto.setPrice(visit.getPrice());
        visitDto.setDate(visit.getDate());
        visitDto.setStartDateTime(visit.getStartDateTime());
        visitDto.setEndDateTime(visit.getEndDateTime());
        visitDto.setStatus( visit.getStatus() );

        return visitDto;
    }
}
