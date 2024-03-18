package com.pomodoro.pomodoromate.studyRoom.models;

import com.pomodoro.pomodoromate.studyRoom.exceptions.InvalidMaxParticipantCountException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class MaxParticipantCount {
    @Column(name = "maxParticipantCount")
    private Integer value;

    public MaxParticipantCount(Integer value) {
        validate(value);

        this.value = value;
    }

    public static MaxParticipantCount of(int value) {
        return new MaxParticipantCount(value);
    }

    private void validate(Integer value) {
        if (value <= 0 || value > 8) {
            throw new InvalidMaxParticipantCountException();
        }
    }

    public int value() {
        return value;
    }
}
