package com.pomodoro.pomodoromate.studyRoom.dtos;

import com.pomodoro.pomodoromate.studyRoom.models.Step;

import java.time.LocalDateTime;

public record StudyRoomDetailDto(
        Long id, String name, String intro, String step,
        Long participantCount, LocalDateTime updateAt) {

    public static StudyRoomDetailDto fake(long id, String name) {
        return new StudyRoomDetailDto(id, name, "설명", Step.PLANNING.toString(), 1L, LocalDateTime.now());
    }
}
