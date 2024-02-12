package com.pomodoro.pomodoromate.studyRoom.models;

import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyAlreadyCompletedException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StudyRoomTest {

    @Test
    void validateCompleted() {
        StudyRoom studyRoom = StudyRoom.builder()
                .id(1L)
                .build();

        assertDoesNotThrow(() -> studyRoom.validateIncomplete());
    }

    @Test
    void validateIncomplete() {
        StudyRoom studyRoom = StudyRoom.builder()
                .id(1L)
                .build();
        studyRoom.complete();

        assertThrows(StudyAlreadyCompletedException.class,
                () -> studyRoom.validateIncomplete());
    }
}
