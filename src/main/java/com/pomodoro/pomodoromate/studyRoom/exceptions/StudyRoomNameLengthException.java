package com.pomodoro.pomodoromate.studyRoom.exceptions;

public class StudyRoomNameLengthException extends RuntimeException {
    public StudyRoomNameLengthException(int minNameLength, int maxNameLength) {
        super("스터디룸의 이름은 최소 " + minNameLength + "자, " +
                "최대 " + maxNameLength + "자 이내로 입력해주세요.");
    }
}
