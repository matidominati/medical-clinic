package com.github.matidominati.medicalclinic.controller;

import com.github.matidominati.medicalclinic.model.dto.DoctorDto;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateDoctorCommand;
import com.github.matidominati.medicalclinic.model.dto.commandDto.editCommand.EditDoctorCommand;
import com.github.matidominati.medicalclinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public List<DoctorDto> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}")
    public DoctorDto getDoctor(@PathVariable Long id) {
        return doctorService.getDoctor(id);
    }

    @PostMapping
    public DoctorDto addDoctor(@RequestBody CreateDoctorCommand createDoctor) {
        return doctorService.addDoctor(createDoctor);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }

    @PutMapping("/{id}")
    public DoctorDto updateDoctor(@PathVariable Long id, @RequestBody EditDoctorCommand updatedDoctor) {
        return doctorService.updateDoctor(id, updatedDoctor);
    }
}
