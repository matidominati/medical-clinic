package com.github.matidominati.medicalclinic.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.matidominati.medicalclinic.model.dto.CreatePatientCommand;
import com.github.matidominati.medicalclinic.model.dto.EditPatientCommand;
import com.github.matidominati.medicalclinic.model.entity.Patient;
import com.github.matidominati.medicalclinic.model.entity.User;
import com.github.matidominati.medicalclinic.repository.PatientRepository;
import com.github.matidominati.medicalclinic.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional

public class PatientControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PatientRepository patientRepository;

    @BeforeEach
    void setup() {
        User user = User.builder()
                .username("Agolota")
                .password("Agolota123")
                .email("andrzej.golota@gmail.com")
                .patient(null)
                .build();
        Patient patient = Patient.builder()
                .id(1)
                .firstName("Andrzej")
                .lastName("Golota")
                .idCardNo("123456")
                .phoneNumber("123-456-789")
                .birthDate(LocalDate.of(1960, 5, 10))
                .user(user)
                .build();

        Optional<Patient> existingPatient = patientRepository.findById(patient.getId());
        if (existingPatient.isEmpty()) {
            patientRepository.save(patient);
        }
    }

    @Test
    @DirtiesContext
    void getAllPatientsTest() throws Exception {
        mockMvc.perform(get("/patients"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Andrzej"))
                .andExpect(jsonPath("$[0].lastName").value("Golota"))
                .andExpect(jsonPath("$[0].phoneNumber").value("123-456-789"))
                .andExpect(jsonPath("$[0].birthDate", Matchers.is(LocalDate.parse("1960-05-10").toString())));
    }

    @Test
    @DirtiesContext
    void getPatientTest() throws Exception {
        mockMvc.perform(get("/patients/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Andrzej"))
                .andExpect(jsonPath("$.lastName").value("Golota"))
                .andExpect(jsonPath("$.phoneNumber").value("123-456-789"))
                .andExpect(jsonPath("$.birthDate", Matchers.is(LocalDate.parse("1960-05-10").toString())));
    }

    @Test
    @Rollback
    @DirtiesContext
    void addPatientTest() throws Exception {
        CreatePatientCommand createPatientCommand = CreatePatientCommand.builder()
                .firstName("Zbigniew")
                .lastName("Durka")
                .idCardNo("123444")
                .phoneNumber("999-456-888")
                .birthDate(LocalDate.of(1966, 8, 28))
                .email("zbigniew.durka@gmail.com")
                .username("Zdurka")
                .password("Zdurkaz123456")
                .build();
        mockMvc.perform(post("/patients")
                        .content(objectMapper.writeValueAsString(createPatientCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Rollback
    @DirtiesContext
    void deletePatientTest() throws Exception {
        mockMvc.perform(delete("/patients/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Rollback
    @DirtiesContext
    void updatePatientTest() throws Exception {
        EditPatientCommand patientUpdatedData = EditPatientCommand.builder()
                .firstName("Rysiek")
                .build();

        mockMvc.perform(put("/patients/{id}", 1)
                        .content(objectMapper.writeValueAsString(patientUpdatedData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(patientUpdatedData.getFirstName()));
//                .andExpect(jsonPath("$.lastName").value(patientUpdatedData.getLastName()));
    }

    @Test
    @Rollback
    @DirtiesContext
    void changePasswordTest() throws Exception {
        EditPatientCommand patientUpdatedData = EditPatientCommand.builder()
                .password("1234Agolota")
                .build();

        mockMvc.perform(patch("/patients/{id}", 1)
                        .content(objectMapper.writeValueAsString(patientUpdatedData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
