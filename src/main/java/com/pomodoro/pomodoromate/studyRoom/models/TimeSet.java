package com.pomodoro.pomodoromate.studyRoom.models;

import com.pomodoro.pomodoromate.studyRoom.dtos.TimeSetDto;
import com.pomodoro.pomodoromate.studyRoom.exceptions.InvalidTimeRangeException;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.pomodoro.pomodoromate.studyRoom.policies.StudyRoomPolicy.MAX_PLANNING_TIME;
import static com.pomodoro.pomodoromate.studyRoom.policies.StudyRoomPolicy.MAX_RESTING_TIME;
import static com.pomodoro.pomodoromate.studyRoom.policies.StudyRoomPolicy.MAX_RETROSPECT_TIME;
import static com.pomodoro.pomodoromate.studyRoom.policies.StudyRoomPolicy.MAX_STUDYING_TIME;
import static com.pomodoro.pomodoromate.studyRoom.policies.StudyRoomPolicy.MIN_PLANNING_TIME;
import static com.pomodoro.pomodoromate.studyRoom.policies.StudyRoomPolicy.MIN_RESTING_TIME;
import static com.pomodoro.pomodoromate.studyRoom.policies.StudyRoomPolicy.MIN_RETROSPECT_TIME;
import static com.pomodoro.pomodoromate.studyRoom.policies.StudyRoomPolicy.MIN_STUDYING_TIME;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeSet {
    @NotNull
    private Integer planningTime;

    @NotNull
    private Integer studyingTime;

    @NotNull
    private Integer retrospectTime;

    @NotNull
    private Integer restingTime;

    public TimeSet(int planningTime, int studyingTime, int retrospectTime, int restingTime) {
        validatePlanningTime(planningTime);
        validateStudyingTime(studyingTime);
        validateRetrospectTime(retrospectTime);
        validateRestingTime(restingTime);

        this.planningTime = planningTime;
        this.studyingTime = studyingTime;
        this.retrospectTime = retrospectTime;
        this.restingTime = restingTime;
    }

    public void validatePlanningTime(Integer time) {
        if (time < MIN_PLANNING_TIME || time > MAX_PLANNING_TIME) {
            throw new InvalidTimeRangeException(Step.PLANNING, MIN_PLANNING_TIME, MAX_PLANNING_TIME);
        }
    }

    public void validateStudyingTime(Integer time) {
        if (time < MIN_STUDYING_TIME || time > MAX_STUDYING_TIME) {
            throw new InvalidTimeRangeException(Step.STUDYING, MIN_PLANNING_TIME, MAX_PLANNING_TIME);
        }
    }

    public void validateRetrospectTime(Integer time) {
        if (time < MIN_RETROSPECT_TIME || time > MAX_RETROSPECT_TIME) {
            throw new InvalidTimeRangeException(Step.RETROSPECT, MIN_PLANNING_TIME, MAX_PLANNING_TIME);
        }
    }

    public void validateRestingTime(Integer time) {
        if (time < MIN_RESTING_TIME || time > MAX_RESTING_TIME) {
            throw new InvalidTimeRangeException(Step.RESTING, MIN_PLANNING_TIME, MAX_PLANNING_TIME);
        }
    }

    public boolean isTimeZero(Step step) {
        return switch (step) {
            case PLANNING -> planningTime == 0;
            case RETROSPECT -> retrospectTime == 0;
            case RESTING -> restingTime == 0;
            default -> false;
        };
    }

    public Integer getTimeOf(Step step) {
        return switch (step) {
            case PLANNING -> planningTime;
            case STUDYING -> studyingTime;
            case RETROSPECT -> retrospectTime;
            case RESTING -> restingTime;
            default -> 0;
        };
    }

    public Integer planningTime() {
        return planningTime;
    }

    public Integer studyingTime() {
        return studyingTime;
    }

    public Integer retrospectTime() {
        return retrospectTime;
    }

    public Integer restingTime() {
        return restingTime;
    }

    public TimeSetDto toDto() {
        return new TimeSetDto(
                planningTime, studyingTime, retrospectTime, restingTime);
    }
}
