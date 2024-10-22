package com.pomodoro.pomodoromate.chat.dtos;

public record ChatSummaryDto(
        Long id, Long roomId, String message, String writer) {
}
