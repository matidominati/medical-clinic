package com.github.matidominati.medicalclinic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class MedicalExceptionHandler {
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Object> handleMedicalException(DataNotFoundException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorResponse.builder()
                .message(ex.getMessage())
                .httpStatus(ex.getHttpStatus())
                .timestamp(ZonedDateTime.now())
                .build());
    }

    @ExceptionHandler(DataAlreadyExistsException.class)
    public ResponseEntity<Object> handleMedicalException(DataAlreadyExistsException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorResponse.builder()
                .message(ex.getMessage())
                .httpStatus(ex.getHttpStatus())
                .timestamp(ZonedDateTime.now())
                .build());
    }

    @ExceptionHandler(ChangeIdException.class)
    public ResponseEntity<Object> handleMedicalException(ChangeIdException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorResponse.builder()
                .message(ex.getMessage())
                .httpStatus(ex.getHttpStatus())
                .timestamp(ZonedDateTime.now())
                .build());
    }

    @ExceptionHandler(IncorrectDateException.class)
    public ResponseEntity<Object> handleMedicalException(IncorrectDateException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorResponse.builder()
                .message(ex.getMessage())
                .httpStatus(ex.getHttpStatus())
                .timestamp(ZonedDateTime.now())
                .build());
    }

    @ExceptionHandler(IncorrectEmailException.class)
    public ResponseEntity<Object> handleMedicalException(IncorrectEmailException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorResponse.builder()
                .message(ex.getMessage())
                .httpStatus(ex.getHttpStatus())
                .timestamp(ZonedDateTime.now())
                .build());
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<Object> handleMedicalException(IncorrectPasswordException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorResponse.builder()
                .message(ex.getMessage())
                .httpStatus(ex.getHttpStatus())
                .timestamp(ZonedDateTime.now())
                .build());
    }

    @ExceptionHandler(IncorrectPhoneNumberException.class)
    public ResponseEntity<Object> handleMedicalException(IncorrectPhoneNumberException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorResponse.builder()
                .message(ex.getMessage())
                .httpStatus(ex.getHttpStatus())
                .timestamp(ZonedDateTime.now())
                .build());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(HttpStatus.BAD_REQUEST + "\n" + "Unknown error");
    }
}
