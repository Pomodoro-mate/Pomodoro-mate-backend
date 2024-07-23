package com.pomodoro.pomodoromate.chat.dtos;

import java.util.List;

public record ChatSummariesDto (
        List<ChatSummaryDto> chatSummaryDtos
) {
}
