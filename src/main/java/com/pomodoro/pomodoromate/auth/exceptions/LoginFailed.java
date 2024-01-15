package com.pomodoro.pomodoromate.auth.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class LoginFailed extends CustomizedException {
    public LoginFailed() {
        super(HttpStatus.BAD_REQUEST, "로그인에 실패했습니다.");
    }
}
