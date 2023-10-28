package com.github.matidominati.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class IllegalVisitOperation extends MedicalException{
    public IllegalVisitOperation(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}

