package com.pomodoro.pomodoromate.studyRoom.dtos;

import com.pomodoro.pomodoromate.participant.dtos.ParticipantSummaryDto;
import com.pomodoro.pomodoromate.studyRoom.models.Step;

import java.time.LocalDateTime;
import java.util.List;

public record StudyRoomDetailDto(
        Long id, String name, String intro, String step, TimeSetDto timeSet,
        List<ParticipantSummaryDto> participantSummaries, LocalDateTime updateAt) {

    public static StudyRoomDetailDto fake(long id, String name) {
        return new StudyRoomDetailDto(id, name, "설명", Step.PLANNING.toString(),
                new TimeSetDto(5, 10, 5, 5), List.of(), LocalDateTime.now());
    }
}
