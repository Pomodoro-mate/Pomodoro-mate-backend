package com.pomodoro.pomodoromate.common.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SessionId {
    @Column(name = "sessionId")
    private String value;

    public SessionId(String value) {
        this.value = value;
    }

    public static SessionId of(String sessionId) {
        return new SessionId(sessionId);
    }

    public String value() {
        return value;
    }
}
