package com.pomodoro.pomodoromate.common.dtos;

public record PageDto(Integer current,
                      Integer total
) {
    public static PageDto of(Integer page, int totalPages) {
        return new PageDto(page, totalPages);
    }
}
