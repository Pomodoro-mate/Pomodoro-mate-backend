package com.pomodoro.pomodoromate.studyRoom.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class StudyRoomIntroLengthException extends CustomizedException {
    public StudyRoomIntroLengthException(int maxIntroLength) {
        super(HttpStatus.BAD_REQUEST, "스터디룸 소개 글은 최대 " + maxIntroLength + "자 이내로 입력해주세요.");
    }
}
