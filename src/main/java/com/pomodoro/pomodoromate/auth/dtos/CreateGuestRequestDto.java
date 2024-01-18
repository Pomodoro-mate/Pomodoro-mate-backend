package com.pomodoro.pomodoromate.auth.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateGuestRequestDto(
        @NotBlank String nickname
) {
}
