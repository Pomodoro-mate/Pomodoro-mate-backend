package com.pomodoro.pomodoromate.participant.applications;

import com.pomodoro.pomodoromate.auth.exceptions.UnauthorizedException;
import com.pomodoro.pomodoromate.common.exceptions.AuthorizationException;
import com.pomodoro.pomodoromate.common.models.Status;
import com.pomodoro.pomodoromate.participant.exceptions.ParticipantNotFoundException;
import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.models.ParticipantId;
import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.applications.CompleteStudyRoomService;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomMismatchException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.models.User;
import com.pomodoro.pomodoromate.user.models.UserId;
import com.pomodoro.pomodoromate.user.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class LeaveStudyServiceTest {
    private ParticipantRepository participantRepository;
    private UserRepository userRepository;
    private StudyRoomRepository studyRoomRepository;
    private CompleteStudyRoomService completeStudyRoomService;
    private LeaveStudyService leaveStudyService;

    @BeforeEach
    void setUp() {
        participantRepository = mock(ParticipantRepository.class);
        userRepository = mock(UserRepository.class);
        studyRoomRepository = mock(StudyRoomRepository.class);
        completeStudyRoomService = mock(CompleteStudyRoomService.class);
        leaveStudyService = new LeaveStudyService(
                participantRepository,
                userRepository,
                studyRoomRepository,
                completeStudyRoomService);
    }

    @Nested
    @DisplayName("성공")
    class Success {
        @Test
        @DisplayName("정상적으로 스터디에서 나가는 경우")
        void leaveStudy() {
            User user = User.builder()
                    .id(1L)
                    .build();

            StudyRoom studyRoom = StudyRoom.builder()
                    .id(1L)
                    .build();

            Participant participant = Participant.builder()
                    .id(1L)
                    .studyRoomId(studyRoom.id())
                    .userId(user.id())
                    .build();

            given(userRepository.findById(user.id().value()))
                    .willReturn(Optional.of(user));

            given(studyRoomRepository.findById(studyRoom.id().value()))
                    .willReturn(Optional.of(studyRoom));

            given(participantRepository.findById(participant.id().value()))
                    .willReturn(Optional.of(participant));

            assertDoesNotThrow(() -> leaveStudyService.leaveStudy(user.id(), studyRoom.id(), participant.id()));

            assertThat(participant.status()).isEqualTo(Status.DELETED);
        }
    }

    @Nested
    @DisplayName("실패")
    class Failure {
        @Test
        @DisplayName("유저를 찾을 수 없는 경우")
        void leaveStudyFailedWithUnauthorized() {
            UserId invalidUserId = UserId.of(999_999L);

            StudyRoomId studyRoomId = StudyRoomId.of(1L);

            ParticipantId participantId = ParticipantId.of(1L);

            given(userRepository.findById(invalidUserId.value()))
                    .willThrow(UnauthorizedException.class);

            assertThrows(UnauthorizedException.class,
                    () -> leaveStudyService.leaveStudy(invalidUserId, studyRoomId, participantId));
        }

        @Test
        @DisplayName("스터디룸을 찾을 수 없는 경우")
        void leaveStudyFailedWithStudyRoomNotFoundException() {
            User user = User.builder()
                    .id(1L)
                    .build();

            StudyRoomId invalidStudyRoomId = StudyRoomId.of(999_999L);

            ParticipantId participantId = ParticipantId.of(1L);

            given(userRepository.findById(user.id().value()))
                    .willReturn(Optional.of(user));

            given(studyRoomRepository.findById(invalidStudyRoomId.value()))
                    .willThrow(StudyRoomNotFoundException.class);

            assertThrows(StudyRoomNotFoundException.class,
                    () -> leaveStudyService.leaveStudy(user.id(), invalidStudyRoomId, participantId));
        }

        @Test
        @DisplayName("참가자를 찾을 수 없는 경우")
        void leaveStudyFailedWithParticipantNotFoundException() {
            User user = User.builder()
                    .id(1L)
                    .build();

            StudyRoom studyRoom = StudyRoom.builder()
                    .id(1L)
                    .build();

            ParticipantId invalidParticipantId = ParticipantId.of(999_999L);

            given(userRepository.findById(user.id().value()))
                    .willReturn(Optional.of(user));

            given(studyRoomRepository.findById(studyRoom.id().value()))
                    .willReturn(Optional.of(studyRoom));

            given(participantRepository.findById(invalidParticipantId.value()))
                    .willThrow(ParticipantNotFoundException.class);

            assertThrows(ParticipantNotFoundException.class,
                    () -> leaveStudyService.leaveStudy(user.id(), studyRoom.id(), invalidParticipantId));
        }

        @Test
        @DisplayName("요청한 스터디룸과 일치하지 않는 경우")
        void leaveStudyFailedWithStudyRoomMismatchException() {
            User user = User.builder()
                    .id(1L)
                    .build();

            StudyRoom studyRoom = StudyRoom.builder()
                    .id(1L)
                    .build();

            StudyRoomId otherStudyRoomId = StudyRoomId.of(999_999L);

            Participant participant = Participant.builder()
                    .id(1L)
                    .studyRoomId(otherStudyRoomId)
                    .userId(user.id())
                    .build();

            given(userRepository.findById(user.id().value()))
                    .willReturn(Optional.of(user));

            given(studyRoomRepository.findById(studyRoom.id().value()))
                    .willReturn(Optional.of(studyRoom));

            given(participantRepository.findById(participant.id().value()))
                    .willReturn(Optional.of(participant));

            assertThrows(StudyRoomMismatchException.class,
                    () -> leaveStudyService.leaveStudy(user.id(), studyRoom.id(), participant.id()));
        }

        @Test
        @DisplayName("요청한 사용자와 참가자가 일치하지 않는 경우")
        void leaveStudyFailedWithAuthorizationException() {
            User user = User.builder()
                    .id(1L)
                    .build();

            StudyRoom studyRoom = StudyRoom.builder()
                    .id(1L)
                    .build();

            UserId otherUserId = UserId.of(999_999L);

            Participant participant = Participant.builder()
                    .id(1L)
                    .studyRoomId(studyRoom.id())
                    .userId(otherUserId)
                    .build();

            given(userRepository.findById(user.id().value()))
                    .willReturn(Optional.of(user));

            given(studyRoomRepository.findById(studyRoom.id().value()))
                    .willReturn(Optional.of(studyRoom));

            given(participantRepository.findById(participant.id().value()))
                    .willReturn(Optional.of(participant));

            assertThrows(AuthorizationException.class,
                    () -> leaveStudyService.leaveStudy(user.id(), studyRoom.id(), participant.id()));
        }
    }
}
