package com.pomodoro.pomodoromate.studyRoom.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class StudyRoomMismatchException extends CustomizedException {
    public StudyRoomMismatchException() {
        super(HttpStatus.BAD_REQUEST, "요청한 스터디룸과 일치하지 않습니다.");
    }
}
