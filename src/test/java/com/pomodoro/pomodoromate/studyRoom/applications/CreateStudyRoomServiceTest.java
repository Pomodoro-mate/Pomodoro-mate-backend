package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.participant.applications.LeaveStudyService;
import com.pomodoro.pomodoromate.studyRoom.dtos.CreateStudyRoomRequest;
import com.pomodoro.pomodoromate.studyRoom.exceptions.ParticipatingRoomExistsException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomInfo;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.applications.ValidateUserService;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CreateStudyRoomServiceTest {
    private StudyRoomRepository studyRoomRepository;
    private ValidateUserService validateUserService;
    private LeaveStudyService leaveStudyService;
    private PasswordEncoder passwordEncoder;
    private CreateStudyRoomService createStudyRoomService;

    @BeforeEach
    void setUp() {
        studyRoomRepository = mock(StudyRoomRepository.class);
        validateUserService = mock(ValidateUserService.class);
        leaveStudyService = mock(LeaveStudyService.class);
        passwordEncoder = mock(Argon2PasswordEncoder.class);
        createStudyRoomService = new CreateStudyRoomService(
                studyRoomRepository, validateUserService, leaveStudyService, passwordEncoder);
    }

    @Nested
    @DisplayName("성공")
    class Success {
        @Test
        void create() {
            StudyRoomInfo info = StudyRoomInfo.of("스터디룸", "같이 공부해요");

            CreateStudyRoomRequest request = CreateStudyRoomRequest.builder()
                    .info(info)
                    .build();

            StudyRoom studyRoom = StudyRoom.builder()
                    .id(1L)
                    .info(info)
                    .build();

            given(studyRoomRepository.save(any()))
                    .willReturn(studyRoom);

            UserId userId = UserId.of(1L);

            Long savedId = createStudyRoomService.create(request, userId);

            assertThat(savedId).isNotNull();
        }

        @Test
        @DisplayName("이미 다른 방에 들어가 있지만 isForce 옵션이 true 인 경우, 생성된 방 id를 반환한다")
        void createWithForceWhenAlreadyParticipating() {
            StudyRoomInfo info = StudyRoomInfo.of("스터디룸", "같이 공부해요");

            CreateStudyRoomRequest request = CreateStudyRoomRequest.builder()
                    .info(info)
                    .isForce(true)
                    .build();

            StudyRoom studyRoom = StudyRoom.builder()
                    .id(1L)
                    .info(info)
                    .build();

            UserId userId = UserId.of(1L);

            given(studyRoomRepository.findParticipatingRoomBy(userId))
                    .willReturn(Optional.of(studyRoom));

            given(studyRoomRepository.save(any()))
                    .willReturn(studyRoom);

            Long savedId = createStudyRoomService.create(request, userId);

            assertThat(savedId).isNotNull();
        }
    }

    @Nested
    @DisplayName("실패")
    class Failure {
        @Test
        @DisplayName("이미 다른 방에 들어가 있는 경우, ParticipatingRoomExists 예외를 반환한다")
        void createFailedWhenAlreadyParticipating() {
            StudyRoomInfo info = StudyRoomInfo.of("스터디룸", "같이 공부해요");

            CreateStudyRoomRequest request = CreateStudyRoomRequest.builder()
                    .info(info)
                    .build();

            StudyRoom studyRoom = StudyRoom.builder()
                    .id(1L)
                    .info(info)
                    .build();

            UserId userId = UserId.of(1L);

            given(studyRoomRepository.findParticipatingRoomBy(userId))
                    .willReturn(Optional.of(studyRoom));

            assertThrows(ParticipatingRoomExistsException.class,
                    () -> createStudyRoomService.create(request, userId));
        }
    }
}
