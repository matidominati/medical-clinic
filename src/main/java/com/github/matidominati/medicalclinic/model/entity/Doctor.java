package com.github.matidominati.medicalclinic.model.entity;

import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateDoctorCommand;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
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

    @OneToMany(mappedBy = "doctor")
    private List<Visit> visits;

    public static Doctor create(CreateDoctorCommand doctorCommand) {
        return Doctor.builder()
                .firstName(doctorCommand.getFirstName())
                .lastName(doctorCommand.getLastName())
                .specialization(doctorCommand.getSpecialization())
                .phoneNumber(doctorCommand.getPhoneNumber())
                .user(User.builder()
                        .email(doctorCommand.getEmail())
                        .username(doctorCommand.getUsername())
                        .password(doctorCommand.getPassword())
                        .build())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(id, doctor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }
}
