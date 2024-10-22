package com.pomodoro.pomodoromate.chat.dtos;

import jakarta.validation.constraints.NotBlank;

public record ChatRequestDto (
        @NotBlank String message,
        @NotBlank String writer,
        @NotBlank Long studyRoomId,
        @NotBlank Long participantId
){
}
