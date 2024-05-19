package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomDetailDto;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomInfo;
import com.pomodoro.pomodoromate.studyRoom.models.TimeSet;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.applications.ValidateUserService;
import com.pomodoro.pomodoromate.user.models.User;
import com.pomodoro.pomodoromate.user.models.UserId;
import com.pomodoro.pomodoromate.user.models.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetStudyRoomServiceTest {
    private StudyRoomRepository studyRoomRepository;
    private ParticipantRepository participantRepository;
    private ValidateUserService validateUserService;
    private GetStudyRoomService getStudyRoomService;

    @BeforeEach
    void setUp() {
        studyRoomRepository = mock(StudyRoomRepository.class);
        participantRepository = mock(ParticipantRepository.class);
        validateUserService = mock(ValidateUserService.class);
        getStudyRoomService = new GetStudyRoomService(
                studyRoomRepository, participantRepository, validateUserService);
    }

    @Test
    void studyRoom() {
        Long studyRoomId = 1L;

        User user = User.builder()
                .id(1L)
                .info(new UserInfo())
                .build();

        StudyRoom studyRoom = StudyRoom.builder()
                .id(studyRoomId)
                .info(new StudyRoomInfo("스터디방 1", "설명"))
                .timeSet(new TimeSet(5, 10, 5, 5))
                .build();

        Participant participant = Participant.builder()
                .id(1L)
                .studyRoomId(studyRoom.id())
                .userId(user.id())
                .userInfo(user.info())
                .build();

        given(studyRoomRepository.findById(studyRoomId))
                .willReturn(Optional.of(studyRoom));

        given(participantRepository.findAllNotDeletedBy(studyRoom.id()))
                .willReturn(List.of(participant));

        UserId userId = UserId.of(1L);

        StudyRoomDetailDto studyRoomDetailDto = getStudyRoomService.studyRoom(studyRoomId, userId);

        assertThat(studyRoomDetailDto.id()).isEqualTo(studyRoomId);
        assertThat(studyRoomDetailDto.name()).isEqualTo(studyRoom.info().name());
    }

    @Test
    void studyRoomWithStudyRoomNotFound() {
        Long invalidStudyRoomId = 999_999L;

        given(studyRoomRepository.findById(invalidStudyRoomId))
                .willThrow(StudyRoomNotFoundException.class);

        UserId userId = UserId.of(1L);

        assertThrows(StudyRoomNotFoundException.class,
                () -> getStudyRoomService.studyRoom(invalidStudyRoomId, userId));
    }
}
