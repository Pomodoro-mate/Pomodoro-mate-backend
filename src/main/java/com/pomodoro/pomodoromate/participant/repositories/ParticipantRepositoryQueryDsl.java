package com.pomodoro.pomodoromate.participant.repositories;

import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;

import java.util.List;

public interface ParticipantRepositoryQueryDsl {
    Long countActiveBy(StudyRoomId studyRoomId);

    List<Participant> findAllActiveBy(StudyRoomId id);
}
