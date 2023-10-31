package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.mapper.DoctorMapper;
import com.github.matidominati.medicalclinic.model.dto.DoctorDto;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateDoctorCommand;
import com.github.matidominati.medicalclinic.model.dto.commandDto.editCommand.EditDoctorCommand;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.repository.DoctorRepository;
import com.github.matidominati.medicalclinic.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.matidominati.medicalclinic.service.validator.CRUDDataValidator.*;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DoctorMapper doctorMapper;

    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::doctorToDoctorDto)
                .collect(Collectors.toList());
    }

    public DoctorDto getDoctor(Long id) {
        Doctor doctor = findByIdOrThrow(id, doctorRepository, Doctor.class);
        return doctorMapper.doctorToDoctorDto(doctor);
    }

    @Transactional
    public DoctorDto addDoctor(CreateDoctorCommand createDoctor) {
        checkData(createDoctor.getFirstName(), createDoctor.getLastName(),
                createDoctor.getPhoneNumber(), createDoctor.getPassword(), createDoctor.getEmail());
        checkIfDataDoesNotExists(createDoctor.getEmail(), createDoctor.getUsername(), userRepository);
        Doctor doctor = Doctor.createDoctor(createDoctor);
        doctorRepository.save(doctor);
        return doctorMapper.doctorToDoctorDto(doctor);
    }

    @Transactional
    public void deleteDoctor(Long id) {
        Doctor doctorToDelete = findByIdOrThrow(id, doctorRepository, Doctor.class);
        doctorRepository.delete(doctorToDelete);
    }

    @Transactional
    public DoctorDto updateDoctor(Long id, EditDoctorCommand updatedDoctor) {
        Doctor doctor = findByIdOrThrow(id, doctorRepository, Doctor.class);
        checkDoctorDataToUpdate(updatedDoctor.getEmail(), updatedDoctor.getPassword(), updatedDoctor.getFirstName(),
                updatedDoctor.getLastName(), updatedDoctor.getPhoneNumber(), doctor);
        doctorRepository.save(doctor);
        return doctorMapper.doctorToDoctorDto(doctor);
    }
}