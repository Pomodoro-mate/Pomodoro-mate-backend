package com.pomodoro.pomodoromate.participant.repositories;

import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;

public interface ParticipantRepositoryQueryDsl {
    Long countActiveByStudyRoomId(StudyRoomId studyRoomId);
}
