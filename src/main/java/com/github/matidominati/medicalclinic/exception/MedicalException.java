package com.github.matidominati.medicalclinic.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public abstract class MedicalException extends RuntimeException{
    private final String message;
    private final HttpStatus httpStatus;
}
