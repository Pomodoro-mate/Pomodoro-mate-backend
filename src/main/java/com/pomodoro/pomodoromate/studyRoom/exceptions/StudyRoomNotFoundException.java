package com.pomodoro.pomodoromate.studyRoom.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class StudyRoomNotFoundException extends CustomizedException {
    public StudyRoomNotFoundException() {
        super(HttpStatus.NOT_FOUND, "스터디룸이 존재하지 않습니다.");
    }
}
