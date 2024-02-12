package com.pomodoro.pomodoromate.studyRoom.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class StudyAlreadyCompletedException extends CustomizedException {
    public StudyAlreadyCompletedException() {
        super(HttpStatus.BAD_REQUEST, "이미 종료된 스터디입니다.");
    }
}
