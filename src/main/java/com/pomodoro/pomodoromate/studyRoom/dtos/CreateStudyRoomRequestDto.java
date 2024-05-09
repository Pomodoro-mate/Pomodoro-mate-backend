package com.pomodoro.pomodoromate.studyRoom.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateStudyRoomRequestDto(
        @NotBlank String name,
        String intro,
//        Boolean isPrivate,
//        String password,
        @NotNull TimeSetDto timeSet,
        boolean isForce
) {
}
