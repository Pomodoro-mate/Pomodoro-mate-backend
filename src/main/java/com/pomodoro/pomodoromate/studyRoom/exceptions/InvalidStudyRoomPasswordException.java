package com.pomodoro.pomodoromate.studyRoom.exceptions;

public class InvalidStudyRoomPasswordException extends RuntimeException {
    public InvalidStudyRoomPasswordException() {
        super("비밀번호는 숫자 4자리 이상, 8자리 이하로 입력해주세요.");
    }
}
