package com.pomodoro.pomodoromate.user.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class InvalidUserNicknameException extends CustomizedException {
    public InvalidUserNicknameException() {
        super(HttpStatus.BAD_REQUEST, "유효하지 않은 닉네임 형식입니다.");
    }
}
