package com.pomodoro.pomodoromate.studyRoom.dtos;

import com.pomodoro.pomodoromate.studyRoom.models.Step;

public record StudyRoomSummaryDto(
        Long id, String name, String intro, String step, Integer participantCount
) {
    public static StudyRoomSummaryDto fake(Long id, String name) {
        return new StudyRoomSummaryDto(id, name, "설명", Step.PLANNING.toString(), 2);
    }
}
