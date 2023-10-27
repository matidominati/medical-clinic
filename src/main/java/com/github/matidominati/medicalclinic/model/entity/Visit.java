package com.github.matidominati.medicalclinic.model.entity;

import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateVisitCommand;
import com.github.matidominati.medicalclinic.model.enums.VisitType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private VisitType status;
    private BigDecimal price;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToOne
    @JoinColumn(name = "institution_id")
    private Institution institution;

    public static Visit createVisit(CreateVisitCommand createVisit) {
        return Visit.builder()
                .status(createVisit.getStatus())
                .startDateTime(createVisit.getStartDateTime())
                .endDateTime(createVisit.getEndDateTime())
                .price(createVisit.getPrice())
                .build();
    }

    public static Visit createVisitWithParameters(Long id, VisitType status, BigDecimal price, LocalDateTime startDateTime,
                                                  LocalDateTime endDateTime, Doctor doctor, Patient patient, Institution institution) {
        return Visit.builder()
                .id(id)
                .status(VisitType.CREATED)
                .price(price)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .doctor(doctor)
                .patient(patient)
                .institution(institution)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visit visit = (Visit) o;
        return id != null && Objects.equals(id, visit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }
}
