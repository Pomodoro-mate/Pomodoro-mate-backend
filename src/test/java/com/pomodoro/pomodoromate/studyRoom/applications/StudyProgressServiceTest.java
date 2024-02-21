package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.studyRoom.exceptions.InvalidStepException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyAlreadyCompletedException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.Step;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.applications.ValidateUserService;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.rmi.StubNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class StudyProgressServiceTest {
    private ValidateUserService validateUserService;
    private StudyRoomRepository studyRoomRepository;
    private StudyProgressService studyProgressService;

    @BeforeEach
    void setUp() {
        validateUserService = mock(ValidateUserService.class);
        studyRoomRepository = mock(StudyRoomRepository.class);
        studyProgressService = new StudyProgressService(
                validateUserService,
                studyRoomRepository
        );
    }

    @Test
    @DisplayName("다음 스터디 단계 진행 성공")
    void proceedToNextStep() {
        UserId userId = UserId.of(1L);

        StudyRoom studyRoom = StudyRoom.builder()
                .id(10L)
                .build();

        given(studyRoomRepository.findById(studyRoom.id().getValue()))
                .willReturn(Optional.of(studyRoom));

        assertDoesNotThrow(() -> studyProgressService
                .proceedToNextStep(userId, studyRoom.id(), Step.PLANNING));

        assertThat(studyRoom.step()).isEqualTo(Step.STUDYING);
    }

    @Test
    @DisplayName("스터디가 존재하지 않는 경우")
    void proceedToNextStepFailedWithStudyRoomNotFoundException() {
        UserId userId = UserId.of(1L);

        StudyRoom studyRoom = StudyRoom.builder()
                .id(10L)
                .build();

        StudyRoomId invalidStudyRoomId = StudyRoomId.of(999_999_999L);

        given(studyRoomRepository.findById(invalidStudyRoomId.getValue()))
                .willThrow(StudyRoomNotFoundException.class);

        assertThrows(StudyRoomNotFoundException.class,
                () -> studyProgressService.proceedToNextStep(userId, invalidStudyRoomId, Step.PLANNING));

        assertThat(studyRoom.step()).isEqualTo(Step.PLANNING);
    }

    @Test
    @DisplayName("스터디가 이미 종료된 경우")
    void proceedToNextStepFailedWithStudyAlreadyCompletedException() {
        UserId userId = UserId.of(1L);

        StudyRoom studyRoom = StudyRoom.builder()
                .id(10L)
                .build();
        studyRoom.complete();

        given(studyRoomRepository.findById(studyRoom.id().getValue()))
                .willReturn(Optional.of(studyRoom));

        assertThrows(StudyAlreadyCompletedException.class,
                () -> studyProgressService.proceedToNextStep(userId, studyRoom.id(), Step.COMPLETED));

        assertThat(studyRoom.step()).isEqualTo(Step.COMPLETED);
    }

    @Test
    @DisplayName("요청한 단계와 현재 진행 중인 단계가 일치하지 않는 경우")
    void proceedToNextStepFailedWithInvalidStepException() {
        UserId userId = UserId.of(1L);

        StudyRoom studyRoom = StudyRoom.builder()
                .id(10L)
                .build();

        given(studyRoomRepository.findById(studyRoom.id().getValue()))
                .willReturn(Optional.of(studyRoom));

        assertThrows(InvalidStepException.class,
                () -> studyProgressService.proceedToNextStep(userId, studyRoom.id(), Step.RETROSPECT));

        assertThat(studyRoom.step()).isEqualTo(Step.PLANNING);
    }
}
