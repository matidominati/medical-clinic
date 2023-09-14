package com.github.matidominati.medicalclinic.personInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@Builder
public class Patient {
    private final String email;
    private String password;
    private final String idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private final LocalDate birthday;

}