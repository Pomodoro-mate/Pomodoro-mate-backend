package com.pomodoro.pomodoromate.auth.applications;

import com.pomodoro.pomodoromate.auth.dtos.TokenDto;
import com.pomodoro.pomodoromate.auth.models.Token;
import com.pomodoro.pomodoromate.auth.repositories.RefreshTokenRepository;
import com.pomodoro.pomodoromate.auth.utils.JwtUtil;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class IssueTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public IssueTokenService(RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public TokenDto issue(UserId userId) {
        String accessToken = jwtUtil.encode(userId);
        String refreshToken = jwtUtil.encode(UUID.randomUUID());

        Token tokenEntity = Token.of(userId, refreshToken);

        refreshTokenRepository.save(tokenEntity);

        return new TokenDto(accessToken, refreshToken);
    }
}
