package com.pomodoro.pomodoromate.studyRoom.models;

import com.pomodoro.pomodoromate.studyRoom.exceptions.InvalidStepException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.MaxParticipantExceededException;
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

    @Test
    void validateCurrentStep() {
        StudyRoom studyRoom = StudyRoom.builder()
                .id(1L)
                .build();

        assertDoesNotThrow(() -> studyRoom.validateCurrentStep(Step.WAITING));

        assertThrows(InvalidStepException.class,
                () -> studyRoom.validateCurrentStep(Step.STUDYING));
    }

    @Test
    void proceedToNextStep() {
        StudyRoom studyRoom = StudyRoom.builder()
                .id(1L)
                .timeSet(new TimeSet(5, 10, 5, 5))
                .build();
        studyRoom.proceedToNextStep();

        assertThat(studyRoom.step()).isEqualTo(Step.PLANNING);

        studyRoom.proceedToNextStep();

        assertThat(studyRoom.step()).isEqualTo(Step.STUDYING);
    }

    @Test
    void proceedToNextStep_nextStepTimeIsZero() {
        StudyRoom studyRoom = StudyRoom.builder()
                .id(1L)
                .timeSet(new TimeSet(0, 10, 5, 5))
                .build();
        studyRoom.proceedToNextStep();

        assertThat(studyRoom.step()).isEqualTo(Step.STUDYING);

        studyRoom.proceedToNextStep();

        assertThat(studyRoom.step()).isEqualTo(Step.RETROSPECT);
    }

    @Test
    void validateMaxParticipantExceeded_NotExceeded() {
        StudyRoom studyRoom = StudyRoom.builder()
                .id(1L)
                .maxParticipantCount(MaxParticipantCount.of(5))
                .build();

        Long participantCount = 1L;

        assertDoesNotThrow(
                () -> studyRoom.validateMaxParticipantExceeded(participantCount));
    }

    @Test
    void validateMaxParticipantExceeded_Exceeded() {
        StudyRoom studyRoom = StudyRoom.builder()
                .id(1L)
                .maxParticipantCount(MaxParticipantCount.of(5))
                .build();

        Long participantCount = 6L;

        assertThrows(MaxParticipantExceededException.class,
                () -> studyRoom.validateMaxParticipantExceeded(participantCount));
    }
}
