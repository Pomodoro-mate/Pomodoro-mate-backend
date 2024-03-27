package com.pomodoro.pomodoromate.participant.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class ParticipantNotInRoomException extends CustomizedException {
    public ParticipantNotInRoomException() {
        super(HttpStatus.FORBIDDEN, "참가자가 현재 채팅방에 참가하지 않은 상태입니다.");
    }
}
