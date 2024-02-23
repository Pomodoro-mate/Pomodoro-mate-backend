package com.pomodoro.pomodoromate.participant.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class ParticipantNotFoundException extends CustomizedException {
    public ParticipantNotFoundException() {
        super(HttpStatus.NOT_FOUND, "참가자가 존재하지 않습니다.");
    }
}
