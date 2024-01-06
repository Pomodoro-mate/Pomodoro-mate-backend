package com.pomodoro.pomodoromate.studyRoom.exceptions;

public class IncorrectStudyRoomPasswordException extends RuntimeException {
    public IncorrectStudyRoomPasswordException() {
        super("비밀번호가 틀렸습니다. 다시 시도해주세요.");
    }
}
