package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.common.exceptions.AuthorizationException;
import com.pomodoro.pomodoromate.participant.exceptions.ParticipantNotInRoomException;
import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.dtos.EditStudyRoomRequest;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomMismatchException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomInfo;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.applications.ValidateUserService;
import com.pomodoro.pomodoromate.user.models.LoginType;
import com.pomodoro.pomodoromate.user.models.User;
import com.pomodoro.pomodoromate.user.models.UserId;
import com.pomodoro.pomodoromate.user.models.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class EditStudyRoomServiceTest {
    private StudyRoomRepository studyRoomRepository;
    private ParticipantRepository participantRepository;
    private ValidateUserService validateUserService;
    private EditStudyRoomService editStudyRoomService;

    @BeforeEach
    void setUp() {
        studyRoomRepository = mock(StudyRoomRepository.class);
        participantRepository = mock(ParticipantRepository.class);
        validateUserService = mock(ValidateUserService.class);
        editStudyRoomService = new EditStudyRoomService(
                studyRoomRepository, participantRepository, validateUserService);
    }

    @Nested
    @DisplayName("성공")
    class Success {
        @Test
        void edit() {
            StudyRoomInfo info = StudyRoomInfo.of("스터디룸", "같이 공부해요");
            StudyRoomInfo editedInfo = StudyRoomInfo.of("스터디룸", "같이 공부할 사람!");

            User user = User.builder()
                    .id(1L)
                    .loginType(LoginType.GUEST)
                    .info(new UserInfo("닉네임"))
                    .build();

            StudyRoom studyRoom = StudyRoom.builder()
                    .id(10L)
                    .info(info)
                    .build();

            Participant participant = Participant.builder()
                    .id(100L)
                    .studyRoomId(studyRoom.id())
                    .userId(user.id())
                    .build();

            EditStudyRoomRequest request = EditStudyRoomRequest.builder()
                    .info(editedInfo)
                    .participantId(participant.id())
                    .build();

            given(studyRoomRepository.findByIdForUpdate(studyRoom.id().value()))
                    .willReturn(Optional.of(studyRoom));

            given(participantRepository.findById(participant.id().value()))
                    .willReturn(Optional.of(participant));

            assertDoesNotThrow(() -> editStudyRoomService.edit(request, studyRoom.id(), user.id()));

            assertThat(studyRoom.info().intro())
                    .isEqualTo(editedInfo.intro());
        }
    }

    @Nested
    @DisplayName("실패")
    class Failure {
        @Test
        @DisplayName("수정자가 스터디에 참가하고 있지 않은 경우 예외 반환")
        void editFailedWithParticipantNotInRoomException() {
            StudyRoomInfo info = StudyRoomInfo.of("스터디룸", "같이 공부해요");
            StudyRoomInfo editedInfo = StudyRoomInfo.of("스터디룸", "같이 공부할 사람!");

            User user = User.builder()
                    .id(1L)
                    .loginType(LoginType.GUEST)
                    .info(new UserInfo("닉네임"))
                    .build();

            StudyRoom studyRoom = StudyRoom.builder()
                    .id(10L)
                    .info(info)
                    .build();

            Participant participant = Participant.builder()
                    .id(100L)
                    .studyRoomId(studyRoom.id())
                    .userId(user.id())
                    .build();
            participant.delete();

            EditStudyRoomRequest request = EditStudyRoomRequest.builder()
                    .info(editedInfo)
                    .participantId(participant.id())
                    .build();

            given(studyRoomRepository.findByIdForUpdate(studyRoom.id().value()))
                    .willReturn(Optional.of(studyRoom));

            given(participantRepository.findById(participant.id().value()))
                    .willReturn(Optional.of(participant));

            assertThrows(ParticipantNotInRoomException.class,
                    () -> editStudyRoomService.edit(request, studyRoom.id(), user.id()));

        }

        @Test
        @DisplayName("참가자와 수정자가 동일하지 않은 경우 예외 반환")
        void editFailedWithAuthorizationException() {
            StudyRoomInfo info = StudyRoomInfo.of("스터디룸", "같이 공부해요");
            StudyRoomInfo editedInfo = StudyRoomInfo.of("스터디룸", "같이 공부할 사람!");

            User user = User.builder()
                    .id(1L)
                    .loginType(LoginType.GUEST)
                    .info(new UserInfo("닉네임"))
                    .build();

            StudyRoom studyRoom = StudyRoom.builder()
                    .id(10L)
                    .info(info)
                    .build();

            UserId otherUserId = UserId.of(999L);

            Participant participant = Participant.builder()
                    .id(100L)
                    .studyRoomId(studyRoom.id())
                    .userId(otherUserId)
                    .build();

            EditStudyRoomRequest request = EditStudyRoomRequest.builder()
                    .info(editedInfo)
                    .participantId(participant.id())
                    .build();

            given(studyRoomRepository.findByIdForUpdate(studyRoom.id().value()))
                    .willReturn(Optional.of(studyRoom));

            given(participantRepository.findById(participant.id().value()))
                    .willReturn(Optional.of(participant));

            assertThrows(AuthorizationException.class,
                    () -> editStudyRoomService.edit(request, studyRoom.id(), user.id()));

        }

        @Test
        @DisplayName("수정자가 다른 스터디 정보를 수정하는 경우 예외 반환")
        void editFailedWithStudyRoomMismatchException() {
            StudyRoomInfo info = StudyRoomInfo.of("스터디룸", "같이 공부해요");
            StudyRoomInfo editedInfo = StudyRoomInfo.of("스터디룸", "같이 공부할 사람!");

            User user = User.builder()
                    .id(1L)
                    .loginType(LoginType.GUEST)
                    .info(new UserInfo("닉네임"))
                    .build();

            StudyRoom studyRoom = StudyRoom.builder()
                    .id(10L)
                    .info(info)
                    .build();

            StudyRoomId otherStudyRoomId = StudyRoomId.of(999L);

            Participant participant = Participant.builder()
                    .id(100L)
                    .studyRoomId(otherStudyRoomId)
                    .userId(user.id())
                    .build();

            EditStudyRoomRequest request = EditStudyRoomRequest.builder()
                    .info(editedInfo)
                    .participantId(participant.id())
                    .build();

            given(studyRoomRepository.findByIdForUpdate(studyRoom.id().value()))
                    .willReturn(Optional.of(studyRoom));

            given(participantRepository.findById(participant.id().value()))
                    .willReturn(Optional.of(participant));

            assertThrows(StudyRoomMismatchException.class,
                    () -> editStudyRoomService.edit(request, studyRoom.id(), user.id()));

        }
    }
}
