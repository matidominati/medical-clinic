package com.github.matidominati.medicalclinic.service.validator;

import com.github.matidominati.medicalclinic.exception.*;
import com.github.matidominati.medicalclinic.model.entity.Doctor;
import com.github.matidominati.medicalclinic.model.entity.Patient;
import com.github.matidominati.medicalclinic.model.entity.User;
import com.github.matidominati.medicalclinic.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class CRUDataValidator {
    public static <T> T findByIdOrThrow(Long id, JpaRepository<T, Long> jpaRepository, String entityName) {
        Optional<T> entity = jpaRepository.findById(id);
        if (entity.isEmpty()) {
            throw new DataNotFoundException(entityName + " with the provided ID does not exist in the database");
        }
        return entity.get();
    }

    public static boolean isPasswordValid(String password, String firstName, String lastName) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        if (password.equals(firstName) || password.equals(lastName)) {
            return false;
        }
        return password.length() > 6;
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.length() == 9;
    }

    public static boolean isEmailValid(String email) {
        return !email.isEmpty();
    }

    public static boolean isFirstNameValid(String firstName) {
        return firstName.length() >= 3;
    }

    public static boolean isLastNameValid(String lastName) {
        return  lastName.length() >= 3;
    }

    public static void checkData(String firstName, String lastName, String phoneNumber,
                                 String password, String email) {

        if (firstName == null || lastName == null || phoneNumber == null || password == null || email == null) {
            throw new IncorrectDataException("One or more required fields are missing") {
            };
        }
        if (!isFirstNameValid(firstName)) {
            throw new IncorrectNameException("Incorrect first name provided");
        }
        if (!isLastNameValid(lastName)) {
            throw new IncorrectNameException("Incorrect last name provided");
        }
        if (!isPasswordValid(password, firstName, lastName)) {
            throw new IncorrectPasswordException("Incorrect password provided");
        }
        if (!isPhoneNumberValid(phoneNumber)) {
            throw new IncorrectPhoneNumberException("Incorrect phone number provided");
        }
        if (!isEmailValid(email)) {
            throw new IncorrectEmailException("Incorrect email provided");
        }
    }

    public static void checkIfDataDoesNotExists(String email, String username, UserRepository userRepository) {
        Optional<User> optionalUserEmail = userRepository.findByEmail(email);
        if (optionalUserEmail.isPresent()) {
            throw new DataAlreadyExistsException("User with given email exist");
        }
        Optional<User> optionalUserUsername = userRepository.findByUsername(username);
        if (optionalUserUsername.isPresent()) {
            throw new DataAlreadyExistsException("User with given username exist");
        }
    }
    public static void checkPatientDataToUpdate(String email, String password, String firstName, String lastName,
                                         String phoneNumber, Patient patient) {
        if (isEmailValid(email) && !email.equals(patient.getUser().getEmail())) {
            patient.getUser().setEmail(email);
        }
        if (isPasswordValid(password, firstName, lastName) && !password.equals(patient.getUser().getPassword())) {
            patient.getUser().setPassword(password);
        }
        if (isFirstNameValid(firstName) && !firstName.equals(patient.getFirstName())) {
            patient.setFirstName(firstName);
        }
        if (isLastNameValid(lastName) && (!lastName.equals(patient.getLastName()))) {
            patient.setLastName(lastName);
        }
        if (isPhoneNumberValid(phoneNumber) && (!phoneNumber.equals(patient.getPhoneNumber()))) {
            patient.setPhoneNumber(phoneNumber);
        }
    }
    public static void checkDoctorDataToUpdate(String email, String password, String firstName, String lastName,
                                                String phoneNumber, Doctor doctor) {
        if (isEmailValid(email) && !email.equals(doctor.getUser().getEmail())) {
            doctor.getUser().setEmail(email);
        }
        if (isPasswordValid(password, firstName, lastName) && !password.equals(doctor.getUser().getPassword())) {
            doctor.getUser().setPassword(password);
        }
        if (isFirstNameValid(firstName) && !firstName.equals(doctor.getFirstName())) {
            doctor.setFirstName(firstName);
        }
        if (isLastNameValid(lastName) && (!lastName.equals(doctor.getLastName()))) {
            doctor.setLastName(lastName);
        }
        if (isPhoneNumberValid(phoneNumber) && (!phoneNumber.equals(doctor.getPhoneNumber()))) {
            doctor.setPhoneNumber(phoneNumber);
        }
    }
}

