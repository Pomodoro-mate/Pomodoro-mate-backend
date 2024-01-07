package com.pomodoro.pomodoromate.common.exceptions;

import org.springframework.http.HttpStatus;

public class CustomizedException extends RuntimeException {
    private final HttpStatus statusCode;

    protected CustomizedException(HttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public String message() {
        return getMessage();
    }

    public HttpStatus statusCode() {
        return statusCode;
    }
}
