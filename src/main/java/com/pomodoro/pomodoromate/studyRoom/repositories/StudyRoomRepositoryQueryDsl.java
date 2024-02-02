package com.pomodoro.pomodoromate.studyRoom.repositories;

import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudyRoomRepositoryQueryDsl {
    Page<StudyRoomSummaryDto> findAllSummaryDto(Pageable pageable);
}
