package com.pomodoro.pomodoromate.studyRoom.repositories;

import com.pomodoro.pomodoromate.common.annotations.LockTimeout;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomSummaryDto;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StudyRoomRepositoryQueryDsl {
    Page<StudyRoomSummaryDto> findAllSummaryDto(Pageable pageable);

    @LockTimeout
    Optional<StudyRoom> findByIdForUpdate(Long id);

    Optional<StudyRoom> findParticipatingRoomBy(UserId userId);
}
