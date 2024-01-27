package com.pomodoro.pomodoromate.auth.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CustomizedException {
    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.");
    }
}
