package com.github.matidominati.medicalclinic.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class DataNotFoundException extends MedicalException{
    public DataNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
