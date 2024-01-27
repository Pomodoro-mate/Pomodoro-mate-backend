package com.pomodoro.pomodoromate.studyRoom.models;

import com.pomodoro.pomodoromate.common.models.EntityId;
import jakarta.persistence.Embeddable;

@Embeddable
public class StudyRoomId extends EntityId {
    public StudyRoomId() {
    }

    public StudyRoomId(Long value) {
        super(value);
    }

    public static StudyRoomId of(Long id) {
        return new StudyRoomId(id);
    }
}
