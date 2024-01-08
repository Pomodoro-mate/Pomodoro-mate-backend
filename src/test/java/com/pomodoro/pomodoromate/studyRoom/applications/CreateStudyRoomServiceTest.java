package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.studyRoom.dtos.CreateStudyRoomRequest;
import com.pomodoro.pomodoromate.studyRoom.dtos.CreateStudyRoomRequestDto;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomInfo;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
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
    private PasswordEncoder passwordEncoder;
    private CreateStudyRoomService createStudyRoomService;

    @BeforeEach
    void setUp() {
        studyRoomRepository = mock(StudyRoomRepository.class);
        passwordEncoder = mock(Argon2PasswordEncoder.class);
        createStudyRoomService = new CreateStudyRoomService(
                studyRoomRepository, passwordEncoder);
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

        Long savedId = createStudyRoomService.create(request);

        assertThat(savedId).isNotNull();
    }
}
