package com.pomodoro.pomodoromate.participant.models;

import com.pomodoro.pomodoromate.common.models.EntityId;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class ParticipantId extends EntityId {
    public ParticipantId() {
    }

    public ParticipantId(Long value) {
        super(value);
    }

    public static ParticipantId of(Long id) {
        return new ParticipantId(id);
    }
}
