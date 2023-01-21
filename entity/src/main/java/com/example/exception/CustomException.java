package com.example.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomException extends Exception {

    private final HttpStatus responseStatus;
    private final ErrorResponse errorResponse;

    public CustomException(String message, HttpStatus responseStatus, ErrorResponse errorResponse) {
        super(message);
        this.responseStatus = responseStatus;
        this.errorResponse = errorResponse;
    }

}