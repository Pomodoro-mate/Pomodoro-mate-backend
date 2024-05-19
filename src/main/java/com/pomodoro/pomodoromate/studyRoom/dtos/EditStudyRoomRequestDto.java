package com.pomodoro.pomodoromate.studyRoom.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record EditStudyRoomRequestDto(
        @NotBlank String name,
        String intro,
        @NotNull TimeSetDto timeSet,
        @NotNull Long participantId
) {
}
