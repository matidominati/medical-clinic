package com.github.matidominati.medicalclinic.controller;

import com.github.matidominati.medicalclinic.controller.dto.PatientDto;
import com.github.matidominati.medicalclinic.controller.mapper.PatientMapper;
import com.github.matidominati.medicalclinic.model.Patient;
import com.github.matidominati.medicalclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import static com.github.matidominati.medicalclinic.controller.mapper.PatientMapper.mapToDto;

@RequiredArgsConstructor
@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public List<PatientDto> getAllPatients() {
        return patientService.getAllPatients().stream()
                .map(patient -> mapToDto(patient))
                .collect(Collectors.toList());
    }

    @GetMapping("/{email}")
    public PatientDto getPatient(@PathVariable String email) {
        Patient patient = patientService.getPatient(email);
        return PatientMapper.mapToDto(patient);
    }

    @PostMapping
    public PatientDto addPatient(@RequestBody Patient patient) {
        patientService.addPatient(patient);
        return mapToDto(patient);
    }

    @DeleteMapping("/{email}")
    public void deletePatient(@PathVariable String email) {
        patientService.deletePatient(email);
    }

    @PutMapping("/{email}")
    public PatientDto updatePatient(@PathVariable String email, @RequestBody Patient updatedPatient) {
       patientService.updatePatient(email, updatedPatient);
       return mapToDto(updatedPatient);
    }

    @PatchMapping("/{email}")
    public PatientDto changePassword(@PathVariable String email, @RequestBody Patient updatedPatient) {
        patientService.changePassword(email, updatedPatient);
        return mapToDto(updatedPatient);
    }
}
