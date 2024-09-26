package com.pomodoro.pomodoromate.auth.applications;

import com.pomodoro.pomodoromate.auth.dtos.TokenDto;
import com.pomodoro.pomodoromate.auth.exceptions.LoginFailed;
import com.pomodoro.pomodoromate.user.models.Email;
import com.pomodoro.pomodoromate.user.models.User;
import com.pomodoro.pomodoromate.user.models.UserInfo;
import com.pomodoro.pomodoromate.user.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Service
public class KakaoLoginService {
    private final UserRepository userRepository;
    private final IssueTokenService issueTokenService;

    public KakaoLoginService(
            UserRepository userRepository,
            IssueTokenService issueTokenService) {
        this.userRepository = userRepository;
        this.issueTokenService = issueTokenService;
    }

    @Transactional
    public TokenDto login(HashMap<String, String> userInformationResponse) {
        try {
            String email = userInformationResponse.get("email");
            String name = userInformationResponse.get("nickname");

            Optional<User> userOptional = userRepository.findByEmail(Email.of(email));

            User user = userOptional.orElseGet(() -> createUser(name, email));

            TokenDto tokenDto = issueTokenService.issue(user.id());

            return tokenDto;
        } catch (Exception e) {
            throw new LoginFailed();
        }
    }

    @Transactional
    private User createUser(String name, String email) {
        User kakaoUser = User.kakao(UserInfo.of(name), Email.of(email));

        User saved = userRepository.save(kakaoUser);

        return saved;
    }
}
