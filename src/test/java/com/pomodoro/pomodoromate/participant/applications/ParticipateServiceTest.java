package com.pomodoro.pomodoromate.participant.applications;

import com.pomodoro.pomodoromate.auth.exceptions.UnauthorizedException;
import com.pomodoro.pomodoromate.participant.dtos.ParticipateRequest;
import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.exceptions.MaxParticipantExceededException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.ParticipatingRoomExistsException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyAlreadyCompletedException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.MaxParticipantCount;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ParticipateServiceTest {
    private ParticipantRepository participantRepository;
    private UserRepository userRepository;
    private StudyRoomRepository studyRoomRepository;
    private LeaveStudyService leaveStudyService;
    private ParticipateService participateService;

    @BeforeEach
    void setUp() {
        participantRepository = mock(ParticipantRepository.class);
        userRepository = mock(UserRepository.class);
        studyRoomRepository = mock(StudyRoomRepository.class);
        leaveStudyService = mock(LeaveStudyService.class);
        participateService = new ParticipateService(
                participantRepository,
                userRepository,
                studyRoomRepository,
                leaveStudyService);
    }

    @Nested
    @DisplayName("성공")
    class Success {
        @Test
        void participate() {
            User user = User.builder()
                    .id(1L)
                    .build();

            StudyRoom studyRoom = StudyRoom.builder()
                    .id(1L)
                    .maxParticipantCount(MaxParticipantCount.of(8))
                    .build();

            Participant participant = Participant.builder()
                    .id(1L)
                    .studyRoomId(studyRoom.id())
                    .userId(user.id())
                    .build();

            ParticipateRequest request = ParticipateRequest.builder()
                    .isForce(false)
                    .build();

            given(userRepository.findById(user.id().value()))
                    .willReturn(Optional.of(user));

            given(studyRoomRepository.findByIdForUpdate(studyRoom.id().value()))
                    .willReturn(Optional.of(studyRoom));

            given(participantRepository.countActiveBy(studyRoom.id()))
                    .willReturn(1L);

            given(participantRepository.save(any()))
                    .willReturn(participant);

            Long savedId = participateService.participate(request, user.id(), studyRoom.id());

            assertThat(savedId).isEqualTo(1L);
        }

        @Test
        @DisplayName("이미 다른 방에 들어가 있지만 isForce 옵션이 true 인 경우, 생성된 방 id를 반환한다")
        void participateWithForceWhenAlreadyParticipating() {
            User user = User.builder()
                    .id(1L)
                    .build();

            StudyRoom studyRoom = StudyRoom.builder()
                    .id(1L)
                    .maxParticipantCount(MaxParticipantCount.of(8))
                    .build();

            Participant participant = Participant.builder()
                    .id(1L)
                    .studyRoomId(studyRoom.id())
                    .userId(user.id())
                    .build();

            ParticipateRequest request = ParticipateRequest.builder()
                    .isForce(true)
                    .build();

            given(studyRoomRepository.findParticipatingRoomBy(user.id()))
                    .willReturn(Optional.of(studyRoom));

            given(userRepository.findById(user.id().value()))
                    .willReturn(Optional.of(user));

            given(studyRoomRepository.findByIdForUpdate(studyRoom.id().value()))
                    .willReturn(Optional.of(studyRoom));

            given(participantRepository.countActiveBy(studyRoom.id()))
                    .willReturn(1L);

            given(participantRepository.save(any()))
                    .willReturn(participant);

            Long savedId = participateService.participate(request, user.id(), studyRoom.id());

            assertThat(savedId).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("실패")
    class Failure {
        @Test
        void participateWithUnauthorized() {
            UserId invalidUserId = UserId.of(999_999L);

            StudyRoomId studyRoomId = StudyRoomId.of(1L);

            ParticipateRequest request = ParticipateRequest.builder()
                    .isForce(false)
                    .build();

            given(userRepository.findById(invalidUserId.value()))
                    .willThrow(UnauthorizedException.class);

            assertThrows(UnauthorizedException.class,
                    () -> participateService.participate(request, invalidUserId, studyRoomId));
        }

        @Test
        void participateWithStudyRoomNotFoundException() {
            User user = User.builder()
                    .id(1L)
                    .build();

            StudyRoomId invalidStudyRoomId = StudyRoomId.of(999_999L);

            ParticipateRequest request = ParticipateRequest.builder()
                    .isForce(false)
                    .build();

            given(userRepository.findById(user.id().value()))
                    .willReturn(Optional.of(user));

            given(studyRoomRepository.findByIdForUpdate(invalidStudyRoomId.value()))
                    .willThrow(StudyRoomNotFoundException.class);

            assertThrows(StudyRoomNotFoundException.class,
                    () -> participateService.participate(request, user.id(), invalidStudyRoomId));
        }

        @Test
        void participateWithStudyAlreadyCompleted() {
            User user = User.builder()
                    .id(1L)
                    .build();

            StudyRoom studyRoom = StudyRoom.builder()
                    .id(1L)
                    .build();

            studyRoom.complete();

            Participant participant = Participant.builder()
                    .id(1L)
                    .studyRoomId(studyRoom.id())
                    .userId(user.id())
                    .build();

            ParticipateRequest request = ParticipateRequest.builder()
                    .isForce(false)
                    .build();

            given(userRepository.findById(user.id().value()))
                    .willReturn(Optional.of(user));

            given(studyRoomRepository.findByIdForUpdate(studyRoom.id().value()))
                    .willReturn(Optional.of(studyRoom));

            given(participantRepository.countActiveBy(studyRoom.id()))
                    .willReturn(1L);

            assertThrows(StudyAlreadyCompletedException.class,
                    () -> participateService.participate(request, user.id(), studyRoom.id()));
        }

        @Test
        void participateWithMaxParticipantsExceededException() {
            User user = User.builder()
                    .id(1L)
                    .build();

            StudyRoom studyRoom = StudyRoom.builder()
                    .id(1L)
                    .maxParticipantCount(MaxParticipantCount.of(8))
                    .build();

            ParticipateRequest request = ParticipateRequest.builder()
                    .isForce(false)
                    .build();

            given(userRepository.findById(user.id().value()))
                    .willReturn(Optional.of(user));

            given(studyRoomRepository.findByIdForUpdate(studyRoom.id().value()))
                    .willReturn(Optional.of(studyRoom));

            given(participantRepository.countActiveBy(studyRoom.id()))
                    .willReturn(studyRoom.maxParticipantCount().longValue());

            assertThrows(MaxParticipantExceededException.class,
                    () -> participateService.participate(request, user.id(), studyRoom.id()));
        }

        @Test
        @DisplayName("이미 다른 방에 들어가 있는 경우, ParticipatingRoomExists 예외를 반환한다")
        void participateFailedWhenAlreadyParticipating() {
            User user = User.builder()
                    .id(1L)
                    .build();

            StudyRoom studyRoom = StudyRoom.builder()
                    .id(1L)
                    .maxParticipantCount(MaxParticipantCount.of(8))
                    .build();

            ParticipateRequest request = ParticipateRequest.builder()
                    .isForce(false)
                    .build();

            given(studyRoomRepository.findParticipatingRoomBy(user.id()))
                    .willReturn(Optional.of(studyRoom));

            given(userRepository.findById(user.id().value()))
                    .willReturn(Optional.of(user));

            assertThrows(ParticipatingRoomExistsException.class,
                    () -> participateService.participate(request, user.id(), studyRoom.id()));
        }
    }
}
