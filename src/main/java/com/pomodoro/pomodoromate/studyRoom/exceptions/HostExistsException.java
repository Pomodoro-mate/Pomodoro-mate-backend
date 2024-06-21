package com.pomodoro.pomodoromate.studyRoom.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class HostExistsException extends CustomizedException {
    public HostExistsException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR , "방장이 존재합니다.");
    }
}
