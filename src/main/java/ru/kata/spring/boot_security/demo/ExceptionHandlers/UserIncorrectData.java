package ru.kata.spring.boot_security.demo.ExceptionHandlers;

import java.time.LocalDateTime;

public class UserIncorrectData {
    private String info;

    private LocalDateTime timeStamp;

    public UserIncorrectData() {
    }

    public UserIncorrectData(String info, LocalDateTime timeStamp) {
        this.info = info;
        this.timeStamp = timeStamp;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
