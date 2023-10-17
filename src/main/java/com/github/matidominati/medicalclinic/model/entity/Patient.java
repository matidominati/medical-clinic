package com.github.matidominati.medicalclinic.model.entity;

import com.github.matidominati.medicalclinic.model.dto.CreatePatientCommand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    private String idCardNo;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDate birthDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    public static Patient create(CreatePatientCommand patientCommand){
        return Patient.builder()
                .firstName(patientCommand.getFirstName())
                .lastName(patientCommand.getLastName())
                .idCardNo(patientCommand.getIdCardNo())
                .phoneNumber(patientCommand.getPhoneNumber())
                .birthDate(patientCommand.getBirthDate())
                .user(User.builder()
                        .email(patientCommand.getEmail())
                        .username(patientCommand.getUsername())
                        .password(patientCommand.getPassword())
                        .build())
                .build();
    }
}