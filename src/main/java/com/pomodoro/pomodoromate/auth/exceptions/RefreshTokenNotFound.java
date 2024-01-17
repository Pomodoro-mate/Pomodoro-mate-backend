package com.pomodoro.pomodoromate.auth.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class RefreshTokenNotFound extends CustomizedException {
    public RefreshTokenNotFound() {
        super(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다.");
    }
}
