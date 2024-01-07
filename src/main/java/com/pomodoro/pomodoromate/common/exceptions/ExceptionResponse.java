package com.pomodoro.pomodoromate.common.exceptions;

public record ExceptionResponse(String message) {
    public static ExceptionResponse of (String message){
        return new ExceptionResponse(message);
    }
}
