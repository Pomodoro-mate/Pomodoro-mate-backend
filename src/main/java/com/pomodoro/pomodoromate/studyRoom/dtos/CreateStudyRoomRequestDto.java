package com.pomodoro.pomodoromate.studyRoom.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateStudyRoomRequestDto(
        @NotBlank String name,
        String intro
//        Boolean isPrivate,
//        String password
) {
}
