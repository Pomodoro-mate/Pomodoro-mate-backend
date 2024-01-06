package com.pomodoro.pomodoromate.studyRoom.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class StudyRoomPassword {
    @Column(name = "password")
    private String value;

    public StudyRoomPassword() {
    }

    public StudyRoomPassword(String value) {
        this.value = value;
    }

    public static StudyRoomPassword of(String password) {
        return new StudyRoomPassword(password);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        StudyRoomPassword otherPassword = (StudyRoomPassword) object;

        return Objects.equals(value, otherPassword.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
