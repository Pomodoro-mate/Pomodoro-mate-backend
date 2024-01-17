package com.pomodoro.pomodoromate.auth.repositories;

import com.pomodoro.pomodoromate.auth.models.Token;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<Token, UserId> {
    Optional<Token> findByNumber(String number);
}
