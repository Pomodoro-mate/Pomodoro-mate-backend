package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomDetailDto;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomInfo;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetStudyRoomServiceTest {
    private StudyRoomRepository studyRoomRepository;
    private ParticipantRepository participantRepository;
    private GetStudyRoomService getStudyRoomService;

    @BeforeEach
    void setUp() {
        studyRoomRepository = mock(StudyRoomRepository.class);
        participantRepository = mock(ParticipantRepository.class);
        getStudyRoomService = new GetStudyRoomService(
                studyRoomRepository, participantRepository);
    }

    @Test
    void studyRoom() {
        Long studyRoomId = 1L;

        StudyRoom studyRoom = StudyRoom.builder()
                .id(studyRoomId)
                .info(new StudyRoomInfo("스터디방 1", "설명"))
                .build();

        given(studyRoomRepository.findById(studyRoomId))
                .willReturn(Optional.of(studyRoom));

        given(participantRepository.countActiveByStudyRoomId(studyRoom.id()))
                .willReturn(1L);

        StudyRoomDetailDto studyRoomDetailDto = getStudyRoomService.studyRoom(studyRoomId);

        assertThat(studyRoomDetailDto.id()).isEqualTo(studyRoomId);
        assertThat(studyRoomDetailDto.name()).isEqualTo(studyRoom.info().name());
    }

    @Test
    void studyRoomWithStudyRoomNotFound() {
        Long invalidStudyRoomId = 999_999L;

        given(studyRoomRepository.findById(invalidStudyRoomId))
                .willThrow(StudyRoomNotFoundException.class);

        assertThrows(StudyRoomNotFoundException.class,
                () -> getStudyRoomService.studyRoom(invalidStudyRoomId));
    }
}
