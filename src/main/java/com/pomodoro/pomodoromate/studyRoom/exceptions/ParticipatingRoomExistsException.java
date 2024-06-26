package com.pomodoro.pomodoromate.studyRoom.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class ParticipatingRoomExistsException extends CustomizedException {
    public ParticipatingRoomExistsException() {
        super(HttpStatus.CONFLICT, "이미 참가중인 방이 존재합니다.");
    }
}
