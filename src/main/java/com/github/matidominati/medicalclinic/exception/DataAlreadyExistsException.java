package com.github.matidominati.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class DataAlreadyExistsException extends MedicalException{
    public DataAlreadyExistsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
