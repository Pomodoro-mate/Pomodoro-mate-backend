package com.pomodoro.pomodoromate.studyRoom.repositories;

import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long>, StudyRoomRepositoryQueryDsl {
}
