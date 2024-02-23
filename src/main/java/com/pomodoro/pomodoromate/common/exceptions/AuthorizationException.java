package com.pomodoro.pomodoromate.common.exceptions;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends CustomizedException {
    public AuthorizationException() {
        super(HttpStatus.FORBIDDEN, "사용자가 요청한 작업을 수행할 권한이 없습니다.");
    }
}
