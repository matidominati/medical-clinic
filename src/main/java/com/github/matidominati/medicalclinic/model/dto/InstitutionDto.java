package com.github.matidominati.medicalclinic.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class InstitutionDto {
    private String name;
    private String address;
    private List<Long> doctorIds;
}
