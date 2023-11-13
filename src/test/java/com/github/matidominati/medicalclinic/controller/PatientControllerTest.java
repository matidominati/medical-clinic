package com.github.matidominati.medicalclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreatePatientCommand;
import com.github.matidominati.medicalclinic.model.dto.commandDto.editCommand.EditPatientCommand;
import com.github.matidominati.medicalclinic.model.entity.Patient;
import com.github.matidominati.medicalclinic.model.entity.User;
import com.github.matidominati.medicalclinic.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
                .id(1L)
                .firstName("Andrzej")
                .lastName("Golota")
                .idCardNo("123456")
                .phoneNumber("123456789")
                .birthDate(LocalDate.of(1960, 5, 10))
                .user(user)
                .build();

        Optional<Patient> existingPatient = patientRepository.findById(patient.getId());
        if (existingPatient.isEmpty()) {
            patientRepository.save(patient);
        }
    }

    @Test
    void getAllPatientsTest() throws Exception {
        mockMvc.perform(get("/patients"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Andrzej"))
                .andExpect(jsonPath("$[0].lastName").value("Golota"))
                .andExpect(jsonPath("$[0].phoneNumber").value("123456789"))
                .andExpect(jsonPath("$[0].birthDate", Matchers.is(LocalDate.parse("1960-05-10").toString())));
    }

    @Test
    void getPatientTest() throws Exception {
        mockMvc.perform(get("/patients/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Andrzej"))
                .andExpect(jsonPath("$.lastName").value("Golota"))
                .andExpect(jsonPath("$.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.birthDate", Matchers.is(LocalDate.parse("1960-05-10").toString())));
    }

    @Test
    @Transactional
    void addPatientTest() throws Exception {
        CreatePatientCommand createPatient = new CreatePatientCommand();
        createPatient.setFirstName("Jan");
        createPatient.setLastName("Nowak");
        createPatient.setBirthDate(LocalDate.of(1990,10,10));
        createPatient.setPassword("jnowak1123");
        createPatient.setUsername(("jnowak123"));
        createPatient.setIdCardNo("123457");
        createPatient.setEmail("jan.nowak@nowa.pl");
        createPatient.setPhoneNumber("123456789");


        MvcResult mvcResult = mockMvc.perform(post("/patients")
                        .content(objectMapper.writeValueAsString(createPatient))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

       CreatePatientCommand patient =  objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreatePatientCommand.class);
       assertThat(patient).isNotNull();
       assertThat(patient.getEmail()).isNull();
       assertThat(patient.getIdCardNo()).isNull();
       assertThat(patient.getFirstName()).isEqualTo("Jan");
    }

    @Test
    @Rollback
    @Transactional
    void deletePatientTest() throws Exception {
        mockMvc.perform(delete("/patients/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void updatePatientTest() throws Exception {
        EditPatientCommand patientUpdatedData = EditPatientCommand.builder()
                .firstName("Rysiek")
                .lastName("xxx")
                .password("rysiek12345")
                .email("rysiek.rysiek@gmail.com")
                .phoneNumber("987654321")
                .build();

        mockMvc.perform(put("/patients/{id}", 1)
                        .content(objectMapper.writeValueAsString(patientUpdatedData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(patientUpdatedData.getFirstName()));
    }

    @Test
    @Transactional
    void changePasswordTest() throws Exception {
        EditPatientCommand patientUpdatedData = EditPatientCommand.builder()
                .password("1234Agolota")
                .build();

        MvcResult mvcResult = mockMvc.perform(patch("/patients/{id}", 1)
                        .content(objectMapper.writeValueAsString(patientUpdatedData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        EditPatientCommand editedPasswordPatient = objectMapper.readValue(mvcResult.getRequest().getContentAsString(), EditPatientCommand.class);
        assertThat(editedPasswordPatient.getPassword()).isEqualTo("1234Agolota");
    }
}
