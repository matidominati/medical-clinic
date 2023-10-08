package com.github.matidominati.medicalclinic.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.matidominati.medicalclinic.enity.Patient;
import com.github.matidominati.medicalclinic.repository.PatientRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
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
    private PatientRepository patientRepository;

    @BeforeEach
    void setup() {
        Patient patient1 = new Patient(1,"andrzej.golota@gmail.com", "a1234567", "12345",
                "Andrzej", "Golota", "123456890", LocalDate.of(1965, 10, 20));
        Optional<Patient> existingPatient = patientRepository.findByEmail(patient1.getEmail());
        if (existingPatient.isEmpty()) {
            patientRepository.save(patient1);
        }
    }

    @Test
    void getAllPatientsTest() throws Exception {
        mockMvc.perform(get("/patients"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("andrzej.golota@gmail.com"))
                .andExpect(jsonPath("$[0].idCardNo").value("12345"))
                .andExpect(jsonPath("$[0].firstName").value("Andrzej"))
                .andExpect(jsonPath("$[0].lastName").value("Golota"))
                .andExpect(jsonPath("$[0].phoneNumber").value("123456890"))
                .andExpect(jsonPath("$[0].birthDate", Matchers.is(LocalDate.parse("1965-10-20").toString())))
                .andExpect(jsonPath("$[0].password").value("a1234567"));
    }

    @Test
    void getPatientTest() throws Exception {
        mockMvc.perform((get("/patients/{email}", "andrzej.golota@gmail.com")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("andrzej.golota@gmail.com"))
                .andExpect(jsonPath("$.idCardNo").value("12345"))
                .andExpect(jsonPath("$.firstName").value("Andrzej"))
                .andExpect(jsonPath("$.lastName").value("Golota"))
                .andExpect(jsonPath("$.phoneNumber").value("123456890"))
                .andExpect(jsonPath("$.birthDate", Matchers.is(LocalDate.parse("1965-10-20").toString())))
                .andExpect(jsonPath("$.password").value("a1234567"));
    }

    @Test
    void addPatientTest() throws Exception {
        Patient patient2 = new Patient(1,"agnieszka.dygala@gmail.com", "a1234567", "123456",
                "Agnieszka", "Dygala", "123456890", LocalDate.of(1965, 10, 20));

        mockMvc.perform(post("/patients")
                        .content(objectMapper.writeValueAsString(patient2))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deletePatientTest() throws Exception {
        mockMvc.perform(delete("/patients/{email}", "andrzej.golota@gmail.com"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updatePatientTest() throws Exception {
        Patient patientUpdated = new Patient(1,"andrzej.golota@gmail.com", "bbb1234567", "12345",
                "Andrzejek", "Golota", "1234500000", LocalDate.of(1965, 10, 20));

        mockMvc.perform(put("/patients/{email}", "andrzej.golota@gmail.com")
                        .content(objectMapper.writeValueAsString(patientUpdated))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("andrzej.golota@gmail.com"))
                .andExpect(jsonPath("$.password").value("bbb1234567"))
                .andExpect(jsonPath("$.firstName").value("Andrzejek"))
                .andExpect(jsonPath("$.phoneNumber").value("1234500000"));
    }

    @Test
    void changePasswordTest() throws Exception {
        Patient patientUpdatedPassword = new Patient(1,"andrzej.golota@gmail.com", "a1234567888", "12345",
                "Andrzej", "Golota", "123456890", LocalDate.of(1965, 10, 20));

        mockMvc.perform(patch("/patients/{email}", "andrzej.golota@gmail.com")
                        .content(objectMapper.writeValueAsString(patientUpdatedPassword))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("andrzej.golota@gmail.com"))
                .andExpect(jsonPath("$.password").value("a1234567888"));
    }
}
