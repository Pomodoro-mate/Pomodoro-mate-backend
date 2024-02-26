package com.pomodoro.pomodoromate.studyRoom.dtos;

import com.pomodoro.pomodoromate.studyRoom.models.Step;

public class StudyRoomSummaryDto {
        private Long id;
        private String name;
        private String intro;
        private String step;
        private Long participantCount;

    public StudyRoomSummaryDto(Long id, String name, String intro, Step step, Long participantCount) {
        this.id = id;
        this.name = name;
        this.intro = intro;
        this.step = step.toString();
        this.participantCount = participantCount;
    }

    public static StudyRoomSummaryDto fake(Long id, String name) {
        return new StudyRoomSummaryDto(id, name, "설명", Step.PLANNING, 2L);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIntro() {
        return intro;
    }

    public String getStep() {
        return step;
    }

    public Long getParticipantCount() {
        return participantCount;
    }
}
