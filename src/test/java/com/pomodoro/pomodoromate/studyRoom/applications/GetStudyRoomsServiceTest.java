package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomSummariesDto;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomSummaryDto;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomInfo;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetStudyRoomsServiceTest {
    private StudyRoomRepository studyRoomRepository;
    private GetStudyRoomsService getStudyRoomsService;

    @BeforeEach
    void setUp() {
        studyRoomRepository = mock(StudyRoomRepository.class);
        getStudyRoomsService = new GetStudyRoomsService(studyRoomRepository);
    }

    @Test
    void studyRooms() {
        int page = 1;

        Pageable pageable = PageRequest.of(page - 1, 8);

        Page<StudyRoom> studyRooms = new PageImpl<>(List.of(
                StudyRoom.builder().id(1L).info(new StudyRoomInfo("스터디방 1", "설명")).build(),
                StudyRoom.builder().id(2L).info(new StudyRoomInfo("스터디방 2", "설명")).build()));

        given(studyRoomRepository.findAll(pageable))
                .willReturn(studyRooms);

        StudyRoomSummariesDto studyRoomSummariesDto = getStudyRoomsService.studyRooms(page);

        assertThat(studyRoomSummariesDto.studyRooms()).hasSize(2);
        assertThat(studyRoomSummariesDto.studyRooms().get(0).name()).isEqualTo("스터디방 1");
    }
}
