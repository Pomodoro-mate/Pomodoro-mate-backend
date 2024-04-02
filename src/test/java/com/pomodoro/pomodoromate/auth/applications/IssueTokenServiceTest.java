package com.pomodoro.pomodoromate.auth.applications;

import com.pomodoro.pomodoromate.auth.dtos.TokenDto;
import com.pomodoro.pomodoromate.auth.exceptions.RefreshTokenNotFound;
import com.pomodoro.pomodoromate.auth.models.Token;
import com.pomodoro.pomodoromate.auth.repositories.RefreshTokenRepository;
import com.pomodoro.pomodoromate.auth.utils.JwtUtil;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class IssueTokenServiceTest {
    private RefreshTokenRepository refreshTokenRepository;
    private IssueTokenService issueTokenService;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        jwtUtil = new JwtUtil("TEST", 6000L, 60000L);
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

    @Test
    void reissue() {
        UserId userId = new UserId(1L);

        String token = jwtUtil.encode(UUID.randomUUID());

        given(refreshTokenRepository.findByNumber(token))
                .willReturn(Optional.of(Token.of(userId, token)));

        TokenDto newToken = issueTokenService.reissue(token);

        assertThat(newToken).isNotNull();
    }

    @Test
    void reissueFail() {
        String token = jwtUtil.encode(UUID.randomUUID());

        given(refreshTokenRepository.findByNumber(token))
                .willReturn(Optional.empty());

        assertThrows(RefreshTokenNotFound.class,
                () -> issueTokenService.reissue(token));
    }
}
