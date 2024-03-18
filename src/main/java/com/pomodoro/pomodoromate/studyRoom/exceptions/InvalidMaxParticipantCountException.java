package com.pomodoro.pomodoromate.studyRoom.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class InvalidMaxParticipantCountException extends CustomizedException {
    public InvalidMaxParticipantCountException() {
        super(HttpStatus.BAD_REQUEST, "최대 참가자 수는 1명 이상 8명 이하로 설정해야 합니다.");
    }
}
