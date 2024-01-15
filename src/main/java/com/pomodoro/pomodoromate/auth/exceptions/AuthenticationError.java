package com.pomodoro.pomodoromate.auth.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class AuthenticationError extends CustomizedException {
    public AuthenticationError() {
        super(HttpStatus.BAD_REQUEST, "Authentication error");
    }
}
