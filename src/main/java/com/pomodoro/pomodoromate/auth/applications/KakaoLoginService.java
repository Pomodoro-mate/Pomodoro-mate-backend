package com.pomodoro.pomodoromate.auth.applications;

import com.pomodoro.pomodoromate.auth.dtos.TokenDto;
import com.pomodoro.pomodoromate.auth.exceptions.LoginFailed;
import com.pomodoro.pomodoromate.auth.utils.KakaoUtil;
import com.pomodoro.pomodoromate.user.models.Email;
import com.pomodoro.pomodoromate.user.models.LoginType;
import com.pomodoro.pomodoromate.user.models.User;
import com.pomodoro.pomodoromate.user.models.UserInfo;
import com.pomodoro.pomodoromate.user.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

@Transactional
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

    public TokenDto login(HashMap<String, String> userInformationResponse) {
        try {
            String email = userInformationResponse.get("email");
            String name = userInformationResponse.get("nickname");

            Optional<User> userOptional = userRepository.findByEmail(email);

            User user = userOptional.orElseGet(() -> createUser(name, email));

            TokenDto tokenDto = issueTokenService.issue(user.id());

            return tokenDto;
        } catch (Exception e) {
            throw new LoginFailed();
        }
    }

    private User createUser(String name, String email) {
        User kakaoUser = User.builder()
                .info(UserInfo.of(name))
                .email(Email.of(email))
                .loginType(LoginType.KAKAO)
                .build();

        User saved = userRepository.save(kakaoUser);

        return saved;
    }
}
