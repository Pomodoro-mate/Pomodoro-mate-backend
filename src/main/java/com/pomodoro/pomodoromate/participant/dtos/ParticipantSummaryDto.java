package com.pomodoro.pomodoromate.participant.dtos;

public record ParticipantSummaryDto(
        Long id, Long userId, String nickname, String imageUrl, boolean isHost) {
}
