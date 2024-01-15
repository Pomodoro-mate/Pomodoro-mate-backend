package com.pomodoro.pomodoromate.auth.applications;

import com.pomodoro.pomodoromate.auth.dtos.TokenDto;
import com.pomodoro.pomodoromate.auth.models.Token;
import com.pomodoro.pomodoromate.auth.repositories.RefreshTokenRepository;
import com.pomodoro.pomodoromate.auth.utils.JwtUtil;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class IssueTokenServiceTest {
    private RefreshTokenRepository refreshTokenRepository;
    private IssueTokenService issueTokenService;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        jwtUtil = new JwtUtil("TEST");
        refreshTokenRepository = mock(RefreshTokenRepository.class);
        issueTokenService = new IssueTokenService(refreshTokenRepository, jwtUtil);
    }

    @Test
    void issue() {
        UserId userId = new UserId(1L);

        TokenDto tokenDto = issueTokenService.issue(userId);

        assertThat(tokenDto.accessToken()).contains(".");
        assertThat(tokenDto.refreshToken()).contains(".");

        verify(refreshTokenRepository).save(any(Token.class));
    }
}
