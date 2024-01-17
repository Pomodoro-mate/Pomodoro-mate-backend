package com.pomodoro.pomodoromate.auth.models;

import com.pomodoro.pomodoromate.auth.utils.JwtUtil;
import com.pomodoro.pomodoromate.user.models.UserId;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;
import java.util.UUID;

@Entity
public class Token {
    @Id
    @Embedded
    private UserId userId;

    private String number;

    public Token() {
    }

    public Token(UserId userId, String number) {
        this.userId = userId;
        this.number = number;
    }

    public static Token of(UserId userId, String refreshToken) {
        return new Token(userId, refreshToken);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Token otherToken = (Token) o;
        return Objects.equals(userId, otherToken.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    public String getNextAccessToken(JwtUtil jwtUtil) {
        return jwtUtil.encode(userId);
    }

    public String getNextVersion(JwtUtil jwtUtil) {
        String tokenNumber = jwtUtil.encode(UUID.randomUUID());

        this.number = tokenNumber;

        return number;
    }
}
