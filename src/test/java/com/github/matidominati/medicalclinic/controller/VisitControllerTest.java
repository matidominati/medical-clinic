package com.github.matidominati.medicalclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateVisitCommand;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.model.entity.Institution;
import com.github.matidominati.medicalclinic.model.entity.Visit;
import com.github.matidominati.medicalclinic.model.enums.VisitType;
import com.github.matidominati.medicalclinic.repository.PatientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VisitControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PatientRepository patientRepository;

    @BeforeEach
    void setup() {
        Institution institution = new Institution(1L, "NFZ", "Warszawa", null);
        Visit visit = Visit.builder()
                .id(1L)
                .startDateTime(LocalDateTime.of(2025, 10, 10, 10, 00))
                .endDateTime(LocalDateTime.of(2025, 10, 10, 10, 30))
                .price(BigDecimal.valueOf(150))
                .doctor(Doctor.builder()
                        .firstName("Andrzej")
                        .lastName("Golota")
                        .phoneNumber("123456789")
                        .institutions(List.of(institution))
                        .build())
                .patient(null)
                .status(VisitType.CREATED)
                .institution(institution)
                .build();
    }

    @Test
    void createVisitTest() throws Exception {
        Institution institution = new Institution(1L, "NFZ", "Warszawa", null);
        CreateVisitCommand createVisit = CreateVisitCommand.builder()
                .startDateTime(LocalDateTime.of(2025, 10, 10, 10, 45))
                .endDateTime(LocalDateTime.of(2025, 10, 10, 11, 30))
                .price(BigDecimal.valueOf(150))
                .status(VisitType.CREATED)
                .institution(institution)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/visits/create")
                        .content(objectMapper.writeValueAsString(createVisit))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        CreateVisitCommand visit = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreateVisitCommand.class);
        assertThat(visit).isNotNull();
    }
}
