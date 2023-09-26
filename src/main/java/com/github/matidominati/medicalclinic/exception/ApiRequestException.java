package com.github.matidominati.medicalclinic.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Getter
public class ApiRequestException extends RuntimeException{
    private final String message;
    private final HttpStatus httpStatus;
}
