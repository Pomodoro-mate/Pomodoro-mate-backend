package com.pomodoro.pomodoromate.participant.repositories;

import com.pomodoro.pomodoromate.participant.models.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long>, ParticipantRepositoryQueryDsl {
}
