package com.pomodoro.pomodoromate.participant.applications;

import com.pomodoro.pomodoromate.participant.dtos.ParticipantSummaryDto;

import java.util.List;

public record ParticipantSummariesDto(
        List<ParticipantSummaryDto> participantSummaries
) {
}
