package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.participant.applications.ParticipateService;
import com.pomodoro.pomodoromate.studyRoom.dtos.CreateStudyRoomRequest;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomInfo;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.applications.ValidateUserService;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ActiveProfiles("test")
class CreateStudyRoomServiceTest {
    private StudyRoomRepository studyRoomRepository;
    private ValidateUserService validateUserService;
    private ParticipateService participateService;
    private PasswordEncoder passwordEncoder;
    private CreateStudyRoomService createStudyRoomService;

    @BeforeEach
    void setUp() {
        studyRoomRepository = mock(StudyRoomRepository.class);
        validateUserService = mock(ValidateUserService.class);
        participateService = mock(ParticipateService.class);
        passwordEncoder = mock(Argon2PasswordEncoder.class);
        createStudyRoomService = new CreateStudyRoomService(
                studyRoomRepository, validateUserService, participateService, passwordEncoder);
    }

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
}
