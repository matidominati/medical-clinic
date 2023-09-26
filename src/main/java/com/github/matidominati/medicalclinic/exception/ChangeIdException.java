package com.github.matidominati.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class ChangeIdException extends IncorrectDataException{
    public ChangeIdException(String message) {
        super(message);
    }
}
