package com.pomodoro.pomodoromate.participant.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class ForbiddenStudyHostActionException extends CustomizedException {
    public ForbiddenStudyHostActionException() {
        super(HttpStatus.FORBIDDEN, "방장만 이 작업을 수행할 권한이 있습니다.");
    }
}
