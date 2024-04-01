package com.pomodoro.pomodoromate.studyRoom.models;

public enum Step {
    WAITING,
    PLANNING,
    STUDYING,
    RETROSPECT,
    RESTING,
    COMPLETED;

    public Step nextStep() {
        return switch (this) {
            case WAITING, RESTING -> PLANNING;
            case PLANNING -> STUDYING;
            case STUDYING -> RETROSPECT;
            case RETROSPECT -> RESTING;
            default -> COMPLETED;
        };
    }
}
