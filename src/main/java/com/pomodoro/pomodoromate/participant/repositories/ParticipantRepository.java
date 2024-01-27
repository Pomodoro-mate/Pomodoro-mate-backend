package com.pomodoro.pomodoromate.participant.repositories;

import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
