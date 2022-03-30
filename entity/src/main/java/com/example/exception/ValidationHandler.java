package com.example.exception;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
//to scan all controllers
@ComponentScan(basePackages="com.example")
public class ValidationHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationException> handleApiRequestException(MethodArgumentNotValidException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ValidationException ex = new ValidationException();
        e.getAllErrors().forEach(error->ex.getErrors().add(error.getDefaultMessage()));
        ex.setHttpStatus(badRequest);
        ex.setTimestamp(ZonedDateTime.now());
        return new ResponseEntity<>(ex,badRequest);

    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<DeletionException> insufficientRights(MethodArgumentNotValidException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        DeletionException ex = new DeletionException();
        e.getAllErrors().forEach(error->ex.getErrors().add(error.getDefaultMessage()));

        ex.setHttpStatus(badRequest);
        ex.setTimestamp(ZonedDateTime.now());
        return new ResponseEntity<>(ex,badRequest);

    }
}
