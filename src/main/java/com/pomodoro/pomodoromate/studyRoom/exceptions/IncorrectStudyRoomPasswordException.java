package com.pomodoro.pomodoromate.studyRoom.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class IncorrectStudyRoomPasswordException extends CustomizedException {
    public IncorrectStudyRoomPasswordException() {
        super(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다. 다시 시도해주세요.");
    }
}
