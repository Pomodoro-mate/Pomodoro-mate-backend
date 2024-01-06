package com.pomodoro.pomodoromate.studyRoom.exceptions;

public class StudyRoomIntroLengthException extends RuntimeException {
    public StudyRoomIntroLengthException(int maxIntroLength) {
        super("스터디룸 소개 글은 최대 " + maxIntroLength + "자 이내로 입력해주세요.");
    }
}
