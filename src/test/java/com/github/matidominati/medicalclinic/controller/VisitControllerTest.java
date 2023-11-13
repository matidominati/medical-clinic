package com.github.matidominati.medicalclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateVisitCommand;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.model.entity.Institution;
import com.github.matidominati.medicalclinic.model.entity.User;
import com.github.matidominati.medicalclinic.model.entity.Visit;
import com.github.matidominati.medicalclinic.model.enums.VisitType;
import com.github.matidominati.medicalclinic.repository.DoctorRepository;
import com.github.matidominati.medicalclinic.repository.InstitutionRepository;
import com.github.matidominati.medicalclinic.repository.PatientRepository;
import com.github.matidominati.medicalclinic.repository.VisitRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VisitControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private InstitutionRepository institutionRepository;


    @Test
    void createVisitTest() throws Exception {
        Institution institution = new Institution(1L, "NFZ", "Warszawa", null);
        institutionRepository.save(institution);
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .specialization("kardiolog")
                .phoneNumber("123456789")
                .specialization("chirurg")
                .institutions(null)
                .user(User.builder()
                        .email("andrzej.golota@gmail.com")
                        .username("Agolota")
                        .password("Agolota123")
                        .build())
                .build();
        doctor.setInstitutions(List.of(institution));
        doctorRepository.save(doctor);

        CreateVisitCommand createVisit = CreateVisitCommand.builder()
                .startDateTime(LocalDateTime.of(2025, 10, 10, 10, 45))
                .endDateTime(LocalDateTime.of(2025, 10, 10, 11, 30))
                .price(BigDecimal.valueOf(150))
                .status(VisitType.CREATED)
                .doctorId(1L)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/visits/create?institutionId=1")
                        .content(objectMapper.writeValueAsString(createVisit))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        CreateVisitCommand visit = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreateVisitCommand.class);
        assertThat(visit).isNotNull();
    }
}
