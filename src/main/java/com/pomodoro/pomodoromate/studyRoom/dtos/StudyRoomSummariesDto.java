package com.pomodoro.pomodoromate.studyRoom.dtos;

import com.pomodoro.pomodoromate.common.dtos.PageDto;
import lombok.Builder;

import java.util.List;

@Builder
public record StudyRoomSummariesDto(
        List<StudyRoomSummaryDto> studyRooms,
        PageDto pageDto) {
}
