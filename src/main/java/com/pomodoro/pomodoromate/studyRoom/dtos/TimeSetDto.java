package com.pomodoro.pomodoromate.studyRoom.dtos;

import jakarta.validation.constraints.NotNull;

public record TimeSetDto(
        @NotNull Integer planningTime,
        @NotNull Integer studyingTime,
        @NotNull Integer retrospectTime,
        @NotNull Integer restingTime
) {
}
