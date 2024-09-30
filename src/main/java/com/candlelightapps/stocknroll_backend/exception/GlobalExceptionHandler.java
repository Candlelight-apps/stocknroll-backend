package com.candlelightapps.stocknroll_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleItemNotFoundException(ItemNotFoundException ie) {
        return new ResponseEntity<>(ie.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleParameterNotDefinedException(ParameterNotDefinedException pe) {
        return new ResponseEntity<>(pe.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
