package com.pomodoro.pomodoromate.user.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class Email {
    @Column(name = "email")
    private String value;

    public Email() {
    }

    public Email(String value) {
        this.value = value;
    }

    public static Email of(String email) {
        return new Email(email);
    }

    public String value() {
        return value;
    }
}
