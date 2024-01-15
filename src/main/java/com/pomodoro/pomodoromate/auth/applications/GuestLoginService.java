package com.pomodoro.pomodoromate.auth.applications;

import com.pomodoro.pomodoromate.auth.dtos.TokenDto;
import com.pomodoro.pomodoromate.auth.exceptions.LoginFailed;
import com.pomodoro.pomodoromate.user.models.User;
import com.pomodoro.pomodoromate.user.models.UserId;
import com.pomodoro.pomodoromate.user.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuestLoginService {
    private final UserRepository userRepository;
    private final IssueTokenService issueTokenService;

    public GuestLoginService(UserRepository userRepository, IssueTokenService issueTokenService) {
        this.userRepository = userRepository;
        this.issueTokenService = issueTokenService;
    }

    @Transactional
    public TokenDto login() {
        try {
            User guest = User.guest();

            User saved = userRepository.save(guest);
            TokenDto tokenDto = issueTokenService.issue(UserId.of(saved.id()));

            return tokenDto;
        } catch (Exception e) {
            throw new LoginFailed();
        }
    }
}
