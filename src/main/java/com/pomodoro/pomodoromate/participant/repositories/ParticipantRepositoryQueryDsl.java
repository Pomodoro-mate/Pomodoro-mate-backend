package com.pomodoro.pomodoromate.participant.repositories;

import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;

import java.util.List;

public interface ParticipantRepositoryQueryDsl {
    Long countActiveByStudyRoomId(StudyRoomId studyRoomId);

    List<Participant> findAllActiveByStudyRoomId(StudyRoomId id);
}
