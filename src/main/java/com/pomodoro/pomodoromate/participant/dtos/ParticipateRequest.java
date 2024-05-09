package com.pomodoro.pomodoromate.participant.dtos;

import lombok.Builder;

public class ParticipateRequest {
    private boolean isForce;

    @Builder
    public ParticipateRequest(boolean isForce) {
        this.isForce = isForce;
    }

    public static ParticipateRequest of(ParticipateRequestDto requestDto) {
        return new ParticipateRequest(
                requestDto.isForce()
        );
    }

    public boolean isForce() {
        return isForce;
    }
}