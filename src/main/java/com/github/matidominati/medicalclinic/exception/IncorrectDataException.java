package com.github.matidominati.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public abstract class  IncorrectDataException extends MedicalException{
    public IncorrectDataException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
