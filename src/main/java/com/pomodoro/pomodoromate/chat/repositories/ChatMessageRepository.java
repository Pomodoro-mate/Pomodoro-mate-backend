package com.pomodoro.pomodoromate.chat.repositories;

import com.pomodoro.pomodoromate.chat.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<Chat, Long>, ChatMessageRepositoryQueryDsl {
}
