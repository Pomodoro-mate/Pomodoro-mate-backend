package com.pomodoro.pomodoromate.studyRoom.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class MaxParticipantExceededException extends CustomizedException {
    public MaxParticipantExceededException() {
        super(HttpStatus.BAD_REQUEST, "최대 참가자 수를 초과했습니다.");
    }
}
