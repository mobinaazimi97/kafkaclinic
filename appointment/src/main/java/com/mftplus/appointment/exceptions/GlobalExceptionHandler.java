package com.mftplus.appointment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleException(Exception ex) {
//        return new ResponseEntity<>("Yek khata etefagh oftade. Lotfan dobare talash konid.", HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    // Handle 400 - Bad Request
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<String> handleBadRequest(Exception ex) {
//        return new ResponseEntity<>("Darkhast sahih nist. Lotfan meghdari sahih vared konid.", HttpStatus.BAD_REQUEST);
//    }
}
