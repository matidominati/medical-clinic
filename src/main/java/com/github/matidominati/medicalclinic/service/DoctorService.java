package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.exception.DataNotFoundException;
import com.github.matidominati.medicalclinic.mapper.DoctorMapper;
import com.github.matidominati.medicalclinic.model.dto.CreateDoctorCommand;
import com.github.matidominati.medicalclinic.model.dto.DoctorDto;
import com.github.matidominati.medicalclinic.model.dto.EditDoctorCommand;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.model.entity.Institution;
import com.github.matidominati.medicalclinic.repository.DoctorRepository;
import com.github.matidominati.medicalclinic.repository.InstitutionRepository;
import com.github.matidominati.medicalclinic.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final InstitutionRepository institutionRepository;
    private final DoctorMapper doctorMapper;
    private final UserRepository userRepository;

    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::doctorToDoctorDto)
                .collect(Collectors.toList());
    }

    public DoctorDto getDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Doctor with given ID does not exists"));
        return doctorMapper.doctorToDoctorDto(doctor);
    }

    @Transactional
    public DoctorDto addDoctor(CreateDoctorCommand createDoctor) {
        DataValidator.checkData(createDoctor.getFirstName(), createDoctor.getLastName(),
                createDoctor.getPhoneNumber(), createDoctor.getPassword(), createDoctor.getEmail());
        DataValidator.checkIfDataNotExists(createDoctor.getEmail(), createDoctor.getUsername(), userRepository);
        Doctor doctor = Doctor.create(createDoctor);
        Institution institution = Institution.builder()
                .name(createDoctor.getInstitutionName())
                .address(createDoctor.getInstitutionAddress())
                .build();
        institutionRepository.save(institution);
        doctor.setInstitutions(List.of(institution));
        doctorRepository.save(doctor);
        return doctorMapper.doctorToDoctorDto(doctor);
    }

    @Transactional
    public void deleteDoctor(Long id) {
        Optional<Doctor> doctorToDelete = doctorRepository.findById(id);
        if (doctorToDelete.isEmpty()) {
            throw new DataNotFoundException("The doctor with the given ID does not exists in the database");
        }
        doctorRepository.delete(doctorToDelete.get());
    }

    @Transactional
    public DoctorDto updateDoctor(Long id, EditDoctorCommand updatedDoctor) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Doctor with the provided ID does not exists"));
        DataValidator.checkDoctorDataToUpdate(updatedDoctor.getEmail(), updatedDoctor.getPassword(), updatedDoctor.getFirstName(),
                updatedDoctor.getLastName(), updatedDoctor.getPhoneNumber(), doctor);
        doctorRepository.save(doctor);
        return doctorMapper.doctorToDoctorDto(doctor);
    }
}