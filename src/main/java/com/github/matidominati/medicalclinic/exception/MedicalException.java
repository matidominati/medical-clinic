package com.github.matidominati.medicalclinic.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Getter
public abstract class MedicalException extends RuntimeException{
    private final HttpStatus httpStatus;

    public MedicalException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
