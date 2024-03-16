package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.Step;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CompleteStudyRoomServiceTest {
    private StudyRoomRepository studyRoomRepository;
    private CompleteStudyRoomService completeStudyRoomService;

    @BeforeEach
    void setUp() {
        studyRoomRepository = mock(StudyRoomRepository.class);
        completeStudyRoomService = new CompleteStudyRoomService(
                studyRoomRepository
        );
    }

    @Test
    void completeStudy() {
        StudyRoom studyRoom = StudyRoom.builder()
                .id(1L)
                .build();

        given(studyRoomRepository.findById(studyRoom.id().value()))
                .willReturn(Optional.of(studyRoom));

        assertDoesNotThrow(() -> completeStudyRoomService.completeStudy(studyRoom.id()));

        assertThat(studyRoom.step()).isEqualTo(Step.COMPLETED);
    }

    @Test
    void completeStudyFailedWithStudyRoomNotFoundException() {
        StudyRoomId invalidStudyRoomId = StudyRoomId.of(999_999L);

        given(studyRoomRepository.findById(invalidStudyRoomId.value()))
                .willThrow(StudyRoomNotFoundException.class);

        assertThrows(StudyRoomNotFoundException.class,
                () -> completeStudyRoomService.completeStudy(invalidStudyRoomId));
    }
}
