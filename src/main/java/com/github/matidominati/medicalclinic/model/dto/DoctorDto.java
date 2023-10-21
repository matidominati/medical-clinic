package com.github.matidominati.medicalclinic.model.dto;

import com.github.matidominati.medicalclinic.model.entity.Institution;
import lombok.Data;

import java.util.List;

@Data
public class DoctorDto {
    private String firstName;
    private String lastName;
    private String specialization;
    private List<Institution> institutions;
}
