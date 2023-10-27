package com.github.matidominati.medicalclinic.controller;

import com.github.matidominati.medicalclinic.model.dto.VisitDto;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateVisitCommand;
import com.github.matidominati.medicalclinic.model.dto.commandDto.editCommand.BookVisitCommand;
import com.github.matidominati.medicalclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/visits")
public class VisitController {
    private final VisitService visitService;

    @PostMapping("/create")
    public VisitDto createVisit(@RequestParam Long institutionId, @RequestBody CreateVisitCommand createVisit) {
        return visitService.createVisit(createVisit, createVisit.getDoctorId(), institutionId);
    }

    @PatchMapping("/book")
    public VisitDto bookVisit(@RequestBody BookVisitCommand bookVisitCommand) {
        return visitService.bookVisit(bookVisitCommand.getPatientId(), bookVisitCommand.getVisitId());
    }

    @GetMapping()
    public List<VisitDto> getAllVisits(@RequestParam Long patientId) {
        return visitService.getAllPatientVisits(patientId);
    }
}