package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.common.dtos.PageDto;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomSummariesDto;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomSummaryDto;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetStudyRoomsService {
    private final StudyRoomRepository studyRoomRepository;

    public GetStudyRoomsService(StudyRoomRepository studyRoomRepository) {
        this.studyRoomRepository = studyRoomRepository;
    }

    @Transactional(readOnly = true)
    public StudyRoomSummariesDto studyRooms(Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 8);

        Page<StudyRoom> studyRooms = studyRoomRepository.findAll(pageable);

        List<StudyRoomSummaryDto> studyRoomSummaryDtos = studyRooms.stream()
                .map(StudyRoom::toSummaryDto).collect(Collectors.toList());

        PageDto pageDto = new PageDto(page, studyRooms.getTotalPages());

        return new StudyRoomSummariesDto(
                studyRoomSummaryDtos, pageDto
        );
    }
}
