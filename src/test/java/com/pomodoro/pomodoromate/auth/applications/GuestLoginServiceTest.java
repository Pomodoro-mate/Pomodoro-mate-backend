package com.pomodoro.pomodoromate.auth.applications;

import com.pomodoro.pomodoromate.auth.dtos.TokenDto;
import com.pomodoro.pomodoromate.user.models.LoginType;
import com.pomodoro.pomodoromate.user.models.User;
import com.pomodoro.pomodoromate.user.models.UserId;
import com.pomodoro.pomodoromate.user.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GuestLoginServiceTest {
    private UserRepository userRepository;
    private GuestLoginService guestLoginService;
    private IssueTokenService issueTokenService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        issueTokenService = mock(IssueTokenService.class);
        guestLoginService = new GuestLoginService(userRepository, issueTokenService);
    }

    @Test
    void loginSuccess() {
        UserId userId = new UserId(1L);

        User user = User.builder()
                .id(userId.getValue())
                .loginType(LoginType.GUEST)
                .build();

        given(userRepository.save(any(User.class)))
                .willReturn(user);

        given(issueTokenService.issue(userId))
                .willReturn(TokenDto.fake());

        TokenDto token = guestLoginService.login();

        assertThat(token).isNotNull();
    }
}