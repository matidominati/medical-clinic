package com.github.matidominati.medicalclinic.model.entity;

import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreatePatientCommand;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

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

    @OneToMany(mappedBy = "patient")
    private List<Visit> visits;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    public static Patient createPatient(CreatePatientCommand patientCommand) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return id != null && Objects.equals(id, patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }
}