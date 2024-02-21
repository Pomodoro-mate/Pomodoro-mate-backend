package com.pomodoro.pomodoromate.studyRoom.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record StudyProgressRequestDto(
        @NotBlank String step
) {
}
