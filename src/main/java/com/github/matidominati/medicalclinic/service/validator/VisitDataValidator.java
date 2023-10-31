package com.github.matidominati.medicalclinic.service.validator;

import com.github.matidominati.medicalclinic.exception.DataAlreadyExistsException;
import com.github.matidominati.medicalclinic.exception.DataNotFoundException;
import com.github.matidominati.medicalclinic.exception.IncorrectDateException;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateVisitCommand;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.model.entity.Patient;
import com.github.matidominati.medicalclinic.model.entity.Visit;
import com.github.matidominati.medicalclinic.model.enums.VisitType;
import com.github.matidominati.medicalclinic.repository.VisitRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VisitDataValidator {

    public static Visit checkIfVisitIsAvailable(Long visitId, VisitRepository visitRepository) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new DataNotFoundException("Visit with provided ID does not exist"));
        if (visit.getStatus() != VisitType.CREATED) {
            throw new DataAlreadyExistsException("Visit is unavailable");
        }
        return visit;
    }

    public static void checkIfPatientCanBookVisit(Visit visit, Patient patient) {
        List<Visit> patientVisits = patient.getVisits();
        boolean haveVisit = patientVisits.stream()
                .anyMatch(patientVisit -> patientVisit.getStartDateTime().isBefore(visit.getEndDateTime())
                        &&
                        patientVisit.getEndDateTime().isAfter(visit.getStartDateTime()
                        ));
        if (haveVisit) {
            throw new DataAlreadyExistsException("Patient has already booked an appointment for this date");
        }
    }

    public static void checkIfVisitTimeIsCorrect(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime.getMinute() % 15 != 0 || endDateTime.getMinute() % 15 != 0) {
            throw new IncorrectDateException("Incorrect visit time");
        }
    }

    public static void checkIfDoctorCanCreateVisit(Doctor doctor, CreateVisitCommand newVisit) {
        List<Visit> existingVisits = doctor.getVisits();
        boolean isOverlap = existingVisits.stream()
                .anyMatch(existingVisit ->
                        existingVisit.getStartDateTime().isBefore(newVisit.getEndDateTime())
                                &&
                                existingVisit.getEndDateTime().isAfter(newVisit.getStartDateTime())
                );
        if (isOverlap) {
            throw new IncorrectDateException("The date of the visit coincides with the existing visit");
        }
    }
}