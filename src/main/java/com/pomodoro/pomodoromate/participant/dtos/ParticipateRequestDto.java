package com.pomodoro.pomodoromate.participant.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ParticipateRequestDto(
        boolean isForce
) {
}
