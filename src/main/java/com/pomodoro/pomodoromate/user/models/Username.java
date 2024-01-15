package com.pomodoro.pomodoromate.user.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class Username {
    @Column(name = "username")
    private String value;

    public Username() {
    }

    public Username(String value) {
        this.value = value;
    }

    public static Username of(String username) {
        return new Username(username);
    }

    public String value() {
        return value;
    }
}
