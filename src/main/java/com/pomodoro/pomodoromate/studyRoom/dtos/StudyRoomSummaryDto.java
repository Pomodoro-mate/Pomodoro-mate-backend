package com.pomodoro.pomodoromate.studyRoom.dtos;

import com.pomodoro.pomodoromate.studyRoom.models.Step;

public record StudyRoomSummaryDto(
        Long id, String name, String step
) {
    public static StudyRoomSummaryDto fake(Long id, String name) {
        return new StudyRoomSummaryDto(id, name, Step.PLANNING.toString());
    }
}
