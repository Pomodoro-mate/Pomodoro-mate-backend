package com.pomodoro.pomodoromate.studyRoom.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class StudyRoomNameLengthException extends CustomizedException {
    public StudyRoomNameLengthException(int minNameLength, int maxNameLength) {
        super(HttpStatus.BAD_REQUEST, "스터디룸의 이름은 최소 " + minNameLength + "자, " +
                "최대 " + maxNameLength + "자 이내로 입력해주세요.");
    }
}
