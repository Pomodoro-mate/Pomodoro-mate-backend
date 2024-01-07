package com.pomodoro.pomodoromate.studyRoom.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import org.springframework.http.HttpStatus;

public class InvalidStudyRoomPasswordException extends CustomizedException {
    public InvalidStudyRoomPasswordException() {
        super(HttpStatus.BAD_REQUEST, "비밀번호는 숫자 4자리 이상, 8자리 이하로 입력해주세요.");
    }
}
