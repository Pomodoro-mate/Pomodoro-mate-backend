package com.pomodoro.pomodoromate.participant.models;

import com.pomodoro.pomodoromate.common.exceptions.AuthorizationException;
import com.pomodoro.pomodoromate.common.models.Status;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomMismatchException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {
    private Participant participant;

    @BeforeEach
    void setUp() {
        participant = Participant.builder()
                .id(1L)
                .studyRoomId(StudyRoomId.of(10L))
                .userId(UserId.of(100L))
                .build();
    }

    @Test
    void delete() {
        assertThat(participant.status()).isEqualTo(Status.ACTIVE);

        participant.delete();

        assertThat(participant.status()).isEqualTo(Status.DELETED);
    }

    @Test
    void validateParticipant() {
        assertDoesNotThrow(() -> participant
                .validateParticipant(participant.userId()));
    }

    @Test
    void validateParticipantFailed() {
        UserId otherUserId = UserId.of(999_999L);

        assertThrows(AuthorizationException.class,
                () -> participant.validateParticipant(otherUserId));
    }

    @Test
    void validateStudyRoom() {
        assertDoesNotThrow(() -> participant
                .validateStudyRoom(participant.studyRoomId()));

    }

    @Test
    void validateStudyRoomFailed() {
        StudyRoomId otherStudyRoomId = StudyRoomId.of(999_999L);

        assertThrows(StudyRoomMismatchException.class,
                () -> participant.validateStudyRoom(otherStudyRoomId));
    }
}
