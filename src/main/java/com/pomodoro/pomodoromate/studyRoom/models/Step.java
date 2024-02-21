package com.pomodoro.pomodoromate.studyRoom.models;

public enum Step {
    PLANNING,
    STUDYING,
    RETROSPECT,
    RESTING,
    COMPLETED;

    public Step nextStep() {
        return switch (this) {
            case PLANNING -> STUDYING;
            case STUDYING -> RETROSPECT;
            case RETROSPECT -> RESTING;
            case RESTING -> PLANNING;
            default -> COMPLETED;
        };
    }
}
