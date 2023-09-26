package com.github.matidominati.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class DataNotFoundException extends MedicalException{
    public DataNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
