package com.github.matidominati.medicalclinic.model.entity;

import com.github.matidominati.medicalclinic.model.dto.CreateDoctorCommand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @ManyToMany
    @JoinTable
            (
                    name = "doctor_institution",
                    joinColumns = @JoinColumn(name = "doctor_id"),
                    inverseJoinColumns = @JoinColumn(name = "institution_id")
            )
    private List<Institution> institutions;

    public static Doctor create(CreateDoctorCommand doctorCommand) {
        return Doctor.builder()
                .firstName(doctorCommand.getFirstName())
                .lastName(doctorCommand.getLastName())
                .specialization(doctorCommand.getSpecialization())
                .institutions(doctorCommand.getInstitutions())
                .phoneNumber(doctorCommand.getPhoneNumber())
                .user(User.builder()
                        .email(doctorCommand.getEmail())
                        .username(doctorCommand.getUsername())
                        .password(doctorCommand.getPassword())
                        .build())
                .build();

    }
}
