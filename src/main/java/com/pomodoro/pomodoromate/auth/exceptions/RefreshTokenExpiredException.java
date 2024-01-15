package com.pomodoro.pomodoromate.auth.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class RefreshTokenExpiredException extends CustomizedException {
    public RefreshTokenExpiredException() {
        super(HttpStatus.UNAUTHORIZED, "만료된 리프레시 토큰입니다.");
    }
}
