package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.config.JpaAuditingConfig;
import com.pomodoro.pomodoromate.participant.exceptions.ParticipantNotInRoomException;
import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.dtos.NextStepStudyRoomDto;
import com.pomodoro.pomodoromate.studyRoom.exceptions.InvalidStepException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyAlreadyCompletedException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.Step;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.studyRoom.models.TimeSet;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.applications.ValidateUserService;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
class StudyProgressServiceTest {
    private ValidateUserService validateUserService;
    private StudyRoomRepository studyRoomRepository;
    private StudyProgressService studyProgressService;
    private ParticipantRepository participantRepository;

    @SpyBean
    private SimpMessagingTemplate messagingTemplate;

    @BeforeEach
    void setUp() {
        validateUserService = mock(ValidateUserService.class);
        studyRoomRepository = mock(StudyRoomRepository.class);
        participantRepository = mock(ParticipantRepository.class);
        studyProgressService = new StudyProgressService(
                validateUserService,
                studyRoomRepository,
                participantRepository,
                messagingTemplate);
    }

    @Test
    @DisplayName("다음 스터디 단계 진행 성공")
    void proceedToNextStep() {
        UserId userId = UserId.of(1L);

        StudyRoom studyRoom = StudyRoom.builder()
                .id(1L)
                .timeSet(new TimeSet(5, 10, 5, 5))
                .build();

        Participant participant = Participant.builder()
                .id(1L)
                .studyRoomId(studyRoom.id())
                .userId(userId)
                .build();

        given(studyRoomRepository.findById(studyRoom.id().value()))
                .willReturn(Optional.of(studyRoom));

        given(participantRepository.findBy(userId, studyRoom.id()))
                .willReturn(Optional.of(participant));

        assertDoesNotThrow(() -> studyProgressService
                .proceedToNextStep(userId, studyRoom.id(), Step.WAITING));

        assertThat(studyRoom.step()).isEqualTo(Step.PLANNING);

        verify(messagingTemplate).convertAndSend(
                eq("/sub/studyrooms/" + studyRoom.id().value() + "/next-step"),
                any(NextStepStudyRoomDto.class)
        );
    }

    @Test
    @DisplayName("스터디가 존재하지 않는 경우")
    void proceedToNextStepFailedWithStudyRoomNotFoundException() {
        UserId userId = UserId.of(1L);

        StudyRoom studyRoom = StudyRoom.builder()
                .id(10L)
                .build();

        StudyRoomId invalidStudyRoomId = StudyRoomId.of(999_999_999L);

        given(studyRoomRepository.findById(invalidStudyRoomId.value()))
                .willThrow(StudyRoomNotFoundException.class);

        assertThrows(StudyRoomNotFoundException.class,
                () -> studyProgressService.proceedToNextStep(userId, invalidStudyRoomId, Step.PLANNING));

        assertThat(studyRoom.step()).isEqualTo(Step.WAITING);
    }

    @Test
    @DisplayName("스터디가 이미 종료된 경우")
    void proceedToNextStepFailedWithStudyAlreadyCompletedException() {
        UserId userId = UserId.of(1L);

        StudyRoom studyRoom = StudyRoom.builder()
                .id(10L)
                .build();
        studyRoom.complete();

        Participant participant = Participant.builder()
                .id(1L)
                .studyRoomId(studyRoom.id())
                .userId(userId)
                .build();

        given(studyRoomRepository.findById(studyRoom.id().value()))
                .willReturn(Optional.of(studyRoom));

        given(participantRepository.findBy(userId, studyRoom.id()))
                .willReturn(Optional.of(participant));

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

        Participant participant = Participant.builder()
                .id(1L)
                .studyRoomId(studyRoom.id())
                .userId(userId)
                .build();

        given(studyRoomRepository.findById(studyRoom.id().value()))
                .willReturn(Optional.of(studyRoom));

        given(participantRepository.findBy(userId, studyRoom.id()))
                .willReturn(Optional.of(participant));

        assertThrows(InvalidStepException.class,
                () -> studyProgressService.proceedToNextStep(userId, studyRoom.id(), Step.RETROSPECT));

        assertThat(studyRoom.step()).isEqualTo(Step.WAITING);
    }

    @DisplayName("요청한 참가자가 현재 채팅방에 참여중이지 않은 경우")
    void proceedToNextStepFailedWithParticipantNotInRoomException() {
        UserId userId = UserId.of(1L);

        StudyRoom studyRoom = StudyRoom.builder()
                .id(10L)
                .build();

        Participant participant = Participant.builder()
                .id(1L)
                .studyRoomId(studyRoom.id())
                .userId(userId)
                .build();
        participant.delete();

        given(studyRoomRepository.findById(studyRoom.id().value()))
                .willReturn(Optional.of(studyRoom));

        given(participantRepository.findBy(userId, studyRoom.id()))
                .willReturn(Optional.of(participant));

        assertThrows(ParticipantNotInRoomException.class,
                () -> studyProgressService.proceedToNextStep(userId, studyRoom.id(), Step.RETROSPECT));

        assertThat(studyRoom.step()).isEqualTo(Step.PLANNING);
    }
}
