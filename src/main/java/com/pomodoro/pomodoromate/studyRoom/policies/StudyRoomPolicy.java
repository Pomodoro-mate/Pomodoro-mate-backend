package com.pomodoro.pomodoromate.studyRoom.policies;

public class StudyRoomPolicy {
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 30;

    public static final int MAX_INTRO_LENGTH = 1000;

    public static final String PASSWORD_PATTERN = "^[0-9]{4,8}$";

    public static final int MIN_PLANNING_TIME = 0;
    public static final int MAX_PLANNING_TIME = 20;

    public static final int MIN_STUDYING_TIME = 10;
    public static final int MAX_STUDYING_TIME = 60;

    public static final int MIN_RETROSPECT_TIME = 0;
    public static final int MAX_RETROSPECT_TIME = 20;

    public static final int MIN_RESTING_TIME = 0;
    public static final int MAX_RESTING_TIME = 20;
}
