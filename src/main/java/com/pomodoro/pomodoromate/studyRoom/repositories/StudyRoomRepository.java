package com.pomodoro.pomodoromate.studyRoom.repositories;

import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long>, StudyRoomRepositoryQueryDsl {
}
