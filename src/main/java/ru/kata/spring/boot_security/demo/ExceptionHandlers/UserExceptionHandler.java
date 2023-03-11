package ru.kata.spring.boot_security.demo.ExceptionHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<UserIncorrectData> handleException(NoSuchUserException e) {
        UserIncorrectData data = new UserIncorrectData(e.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<UserIncorrectData> handleException(Exception e) {
        UserIncorrectData data = new UserIncorrectData(e.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<UserIncorrectData> handleException(UserNotCreated e) {
        UserIncorrectData data = new UserIncorrectData(e.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
