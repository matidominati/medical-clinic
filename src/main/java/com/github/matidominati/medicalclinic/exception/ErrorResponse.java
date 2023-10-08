package com.github.matidominati.medicalclinic.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@Builder
public class ErrorResponse {
    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;
}
