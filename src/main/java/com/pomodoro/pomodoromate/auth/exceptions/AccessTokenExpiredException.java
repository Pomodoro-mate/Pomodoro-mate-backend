package com.pomodoro.pomodoromate.auth.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class AccessTokenExpiredException extends CustomizedException {
    public AccessTokenExpiredException() {
        super(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.");
    }
}
