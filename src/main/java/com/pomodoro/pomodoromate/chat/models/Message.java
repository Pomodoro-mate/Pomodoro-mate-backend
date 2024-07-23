package com.pomodoro.pomodoromate.chat.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class Message {
    @Column(name = "chatMessage")
    private String value;

    public Message() {
    }

    public Message(String value) {
        this.value = value;
    }

    public static Message of(String message) {
        return new Message(message);
    }

    public String value() {
        return value;
    }
}
