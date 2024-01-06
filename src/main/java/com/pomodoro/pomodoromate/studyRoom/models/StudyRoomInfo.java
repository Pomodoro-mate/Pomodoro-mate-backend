package com.pomodoro.pomodoromate.studyRoom.models;

import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomIntroLengthException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNameLengthException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

import static com.pomodoro.pomodoromate.studyRoom.policies.StudyRoomPolicy.MAX_INTRO_LENGTH;
import static com.pomodoro.pomodoromate.studyRoom.policies.StudyRoomPolicy.MAX_NAME_LENGTH;
import static com.pomodoro.pomodoromate.studyRoom.policies.StudyRoomPolicy.MIN_NAME_LENGTH;

@Embeddable
public class StudyRoomInfo {
    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "intro", length = 1000)
    private String intro;

    public StudyRoomInfo() {
    }

    public StudyRoomInfo(String name, String intro) {
        validateName(name);
        validateIntro(intro);

        this.name = name;
        this.intro = intro;
    }

    public void validateName(String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new StudyRoomNameLengthException(MIN_NAME_LENGTH, MAX_NAME_LENGTH);
        }
    }

    public void validateIntro(String intro) {
        if (intro.length() > MAX_INTRO_LENGTH) {
            throw new StudyRoomIntroLengthException(MAX_INTRO_LENGTH);
        }
    }

    public String name() {
        return name;
    }

    public String intro() {
        return intro;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        StudyRoomInfo other = (StudyRoomInfo) object;

        return Objects.equals(name, other.name)
                && Objects.equals(intro, other.intro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, intro);
    }
}
