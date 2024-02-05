package com.pomodoro.pomodoromate.user.applications;

import com.pomodoro.pomodoromate.auth.exceptions.UnauthorizedException;
import com.pomodoro.pomodoromate.user.models.UserId;
import com.pomodoro.pomodoromate.user.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ValidateUserService {
    private final UserRepository userRepository;

    public ValidateUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validate(UserId userId) {
        if (!userRepository.existsById(userId.getValue())) {
            throw new UnauthorizedException();
        }
    }
}
