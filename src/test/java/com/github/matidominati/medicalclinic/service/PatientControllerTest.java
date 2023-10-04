package com.github.matidominati.medicalclinic.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.matidominati.medicalclinic.model.Patient;
import com.github.matidominati.medicalclinic.repository.PatientRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private PatientRepositoryImpl patientRepository;

    Patient patient1 = new Patient("andrzej.golota@gmail.com", "a1234567", "12345",
            "Andrzej", "Golota", "123456890", LocalDate.of(1965, 10, 20));

    @BeforeEach
    void setup() {
        Optional<Patient> existingPatient = patientRepository.findPatientByEmail(patient1.getEmail());
        if (existingPatient.isEmpty()) {
            patientRepository.save(patient1);
        }
    }

    @Test
    void getAllPatientsTest() throws Exception {
        mockMvc.perform(get("/patients"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(patient1.getEmail()))
                .andExpect(jsonPath("$[0].idCardNo").value(patient1.getIdCardNo()))
                .andExpect(jsonPath("$[0].firstName").value(patient1.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(patient1.getLastName()))
                .andExpect(jsonPath("$[0].phoneNumber").value(patient1.getPhoneNumber()))
                .andExpect(jsonPath("$[0].birthDate").value(patient1.getBirthDate()))
                .andExpect(jsonPath("$[0].password").value(patient1.getPassword()));
    }

    @Test
    void getPatientTest() throws Exception {
        mockMvc.perform((get("/patients/{email}", patient1.getEmail())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(patient1.getEmail()))
                .andExpect(jsonPath("$.idCardNo").value(patient1.getIdCardNo()))
                .andExpect(jsonPath("$.firstName").value(patient1.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(patient1.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(patient1.getPhoneNumber()))
                .andExpect(jsonPath("$.birthDate").value(patient1.getBirthDate()))
                .andExpect(jsonPath("$.password").value(patient1.getPassword()));
    }

    @Test
    void addPatientTest() throws Exception {
        Patient patient2 = new Patient("agnieszka.dygala@gmail.com", "a1234567", "123456",
                "Agnieszka", "Dygala", "123456890", LocalDate.of(1965, 10, 20));

        mockMvc.perform(post("/patients")
                        .content(objectMapper.writeValueAsString(patient2))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deletePatientTest() throws Exception {
        mockMvc.perform(delete("/patients/{email}", patient1.getEmail()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updatePatientTest() throws Exception {
        Patient patientUpdated = new Patient("andrzej.golota@gmail.com", "bbb1234567", "12345",
                "Andrzejek", "Golota", "1234500000", LocalDate.of(1965, 10, 20));

        mockMvc.perform(put("/patients/{email}", patient1.getEmail())
                        .content(objectMapper.writeValueAsString(patientUpdated))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(patient1.getEmail()))
                .andExpect(jsonPath("$.password").value(patientUpdated.getPassword()))
                .andExpect(jsonPath("$.firstName").value(patientUpdated.getFirstName()))
                .andExpect(jsonPath("$.phoneNumber").value(patientUpdated.getPhoneNumber()));
    }

    @Test
    void changePasswordTest() throws Exception {
        Patient patientUpdatedPassword = new Patient("andrzej.golota@gmail.com", "a1234567888", "12345",
                "Andrzej", "Golota", "123456890", LocalDate.of(1965, 10, 20));

        mockMvc.perform(patch("/patients/{email}", patient1.getEmail())
                        .content(objectMapper.writeValueAsString(patientUpdatedPassword))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(patient1.getEmail()))
                .andExpect(jsonPath("$.password").value(patientUpdatedPassword.getPassword()));
    }
}
