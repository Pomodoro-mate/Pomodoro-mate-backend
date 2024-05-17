package com.pomodoro.pomodoromate.studyRoom.dtos;

import com.pomodoro.pomodoromate.participant.models.ParticipantId;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomInfo;
import com.pomodoro.pomodoromate.studyRoom.models.TimeSet;
import lombok.Builder;

public class EditStudyRoomRequest {
    private StudyRoomInfo info;
    private TimeSet timeSet;
    private ParticipantId participantId;

    @Builder
    public EditStudyRoomRequest(StudyRoomInfo info, TimeSet timeSet, ParticipantId participantId) {
        this.info = info;
        this.timeSet = timeSet;
        this.participantId = participantId;
    }

    public static EditStudyRoomRequest of(EditStudyRoomRequestDto requestDto) {
        return new EditStudyRoomRequest(
                new StudyRoomInfo(requestDto.name(), requestDto.intro()),
                new TimeSet(requestDto.timeSet().planningTime(),
                        requestDto.timeSet().studyingTime(),
                        requestDto.timeSet().retrospectTime(),
                        requestDto.timeSet().restingTime()),
                new ParticipantId(requestDto.participantId())
        );
    }

    public StudyRoomInfo getInfo() {
        return info;
    }

    public TimeSet getTimeSet() {
        return timeSet;
    }

    public ParticipantId getParticipantId() {
        return participantId;
    }
}
