package com.example.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ValidationException  {
    private  List<String> errors = new ArrayList<>();
    private  HttpStatus httpStatus;
    private  ZonedDateTime timestamp;




}
