package com.pomodoro.pomodoromate.chat.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class Writer {
    @Column(name = "message")
    private String value;

    public Writer() {
    }

    public Writer(String value) {
        this.value = value;
    }

    public static Writer of(String writer) {
        return new Writer(writer);
    }

    public String value() {
        return value;
    }
}
