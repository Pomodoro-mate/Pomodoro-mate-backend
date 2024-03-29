package com.pomodoro.pomodoromate.studyRoom.exceptions;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import com.pomodoro.pomodoromate.studyRoom.models.Step;
import org.springframework.http.HttpStatus;



public class InvalidTimeRangeException extends CustomizedException {
    public InvalidTimeRangeException(Step step, int minTime, int maxTime) {
        super(HttpStatus.BAD_REQUEST,
                generateErrorMessage(step, minTime, maxTime));
    }

    private static String generateErrorMessage(Step step, int minTime, int maxTime) {
        return switch (step) {
            case PLANNING -> "계획 시간은 최소 " + minTime + "분, 최대 " + maxTime + "분 이내로 입력해주세요.";
            case STUDYING -> "스터디 시간은 최소 " + minTime + "분, 최대 " + maxTime + "분 이내로 입력해주세요.";
            case RETROSPECT -> "회고 시간은 최소 " + minTime + "분, 최대 " + maxTime + "분 이내로 입력해주세요.";
            case RESTING -> "휴식 시간은 최소 " + minTime + "분, 최대 " + maxTime + "분 이내로 입력해주세요.";
            default -> "유효하지 않은 스텝입니다.";
        };
    }
}
