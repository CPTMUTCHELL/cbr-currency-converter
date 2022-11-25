package com.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
//to scan all controllers
@ComponentScan(basePackages="com.example")
@Slf4j
public class ValidationHandler {

    @ExceptionHandler({CustomException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        log.error("Exception is handled - Response {}, debugMessage: {} ",
                ex.getResponseStatus().value(),
                ex.getMessage());

        return new ResponseEntity<>(ex.getErrorResponse(), ex.getResponseStatus());
    }

//    @ExceptionHandler({ConstraintViolationException.class})
//    @ResponseBody
//    public ResponseEntity<ErrorResponse> handleValidationException(ConstraintViolationException ex) {
//        var msg= new StringBuilder();
//        ex.getConstraintViolations().forEach(el->msg.append(el.getMessage()));
//        log.error("Exception is handled - Response {}, message: {}",
//                ex.getMessage(),msg);
//        var customEx = new CustomException(msg.toString(), HttpStatus.BAD_REQUEST,new ErrorResponse(msg.toString()));
//        return new ResponseEntity<>(customEx.getErrorResponse(), customEx.getResponseStatus());
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleUserCredentialsValidation(MethodArgumentNotValidException ex) {
        var msg = new StringBuilder();
        msg.append(ex.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", ")));
        log.error("Exception is handled - debugMessage: {} ",
                msg);

        var badRequest = HttpStatus.BAD_REQUEST;
        var customEx = new CustomException(msg.toString(), badRequest,new ErrorResponse(msg.toString()));

        return new ResponseEntity<>(customEx.getErrorResponse(), customEx.getResponseStatus());
    }
}
