package ru.kata.spring.boot_security.demo.ExceptionHandlers;

public class UserNotCreated extends RuntimeException {
    public UserNotCreated() {
    }

    public UserNotCreated(String message) {
        super(message);
    }
}
