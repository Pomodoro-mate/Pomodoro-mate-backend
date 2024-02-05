package com.pomodoro.pomodoromate.user.applications;

import com.pomodoro.pomodoromate.auth.exceptions.UnauthorizedException;
import com.pomodoro.pomodoromate.user.models.User;
import com.pomodoro.pomodoromate.user.models.UserId;
import com.pomodoro.pomodoromate.user.models.UserInfo;
import com.pomodoro.pomodoromate.user.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ValidateUserServiceTest {
    private UserRepository userRepository;
    private ValidateUserService validateUserService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        validateUserService = new ValidateUserService(userRepository);
    }

    @Test
    void validateSuccess() {
        UserId userId = UserId.of(1L);

        given(userRepository.existsById(userId.getValue()))
                .willReturn(true);

        assertDoesNotThrow(() -> validateUserService.validate(userId));
    }

    @Test
    void validateWithUnauthorized() {
        UserId invalidUserId = UserId.of(999_999L);

        given(userRepository.existsById(invalidUserId.getValue()))
                .willReturn(false);

        assertThrows(UnauthorizedException.class,
                () -> validateUserService.validate(invalidUserId));
    }
}
