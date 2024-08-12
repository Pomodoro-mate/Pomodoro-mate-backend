package com.pomodoro.pomodoromate.chat.repositories;

import com.pomodoro.pomodoromate.chat.models.Chat;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;

import java.util.List;

public interface ChatRepositoryQueryDsl {
    List<Chat> findAllBy(StudyRoomId studyRoomId);
}
