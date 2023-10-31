package com.github.matidominati.medicalclinic.model.entity;

import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateInstitutionCommand;
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
public class Institution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String name;
    private String address;
    @ManyToMany(mappedBy = "institutions")
    private List<Doctor> doctors;

    public static Institution createInstitution(CreateInstitutionCommand createInstitution) {
        return Institution.builder()
                .name(createInstitution.getName())
                .address(createInstitution.getAddress())
                .doctors(createInstitution.getDoctors())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Institution that = (Institution) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }
}
