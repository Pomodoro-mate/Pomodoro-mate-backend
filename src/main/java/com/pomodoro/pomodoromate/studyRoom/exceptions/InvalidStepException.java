package com.pomodoro.pomodoromate.studyRoom.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class InvalidStepException extends CustomizedException {
    public InvalidStepException() {
        super(HttpStatus.BAD_REQUEST, "요청한 단계와 현재 진행 중인 단계가 일치하지 않습니다.");
    }
}
