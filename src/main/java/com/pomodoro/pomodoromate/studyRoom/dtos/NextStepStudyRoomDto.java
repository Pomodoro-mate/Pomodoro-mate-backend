package com.pomodoro.pomodoromate.studyRoom.dtos;

import java.time.LocalDateTime;

public record NextStepStudyRoomDto(
        Long id, String step, LocalDateTime updateAt) {
}
