package com.pomodoro.pomodoromate.auth.applications;

import com.pomodoro.pomodoromate.auth.dtos.GoogleInfoResponse;
import com.pomodoro.pomodoromate.auth.dtos.GoogleResponse;
import com.pomodoro.pomodoromate.auth.dtos.TokenDto;
import com.pomodoro.pomodoromate.auth.exceptions.LoginFailed;
import com.pomodoro.pomodoromate.auth.utils.GoogleUtil;
import com.pomodoro.pomodoromate.user.models.Email;
import com.pomodoro.pomodoromate.user.models.LoginType;
import com.pomodoro.pomodoromate.user.models.User;
import com.pomodoro.pomodoromate.user.models.UserInfo;
import com.pomodoro.pomodoromate.user.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Transactional
@Service
public class GoogleLoginService {
    private final UserRepository userRepository;
    private final IssueTokenService issueTokenService;
    private final GoogleUtil googleUtil;

    public GoogleLoginService(
            UserRepository userRepository,
            IssueTokenService issueTokenService,
            GoogleUtil googleUtil
    ) {
        this.userRepository = userRepository;
        this.issueTokenService = issueTokenService;
        this.googleUtil = googleUtil;
    }

    public TokenDto login(GoogleInfoResponse userInformationResponse) {
        try {
            String email = userInformationResponse.email();
            String name = userInformationResponse.name();

            Optional<User> userOptional = userRepository.findByEmail(email);

            User user = userOptional.orElseGet(() -> createUser(name, email));

            TokenDto tokenDto = issueTokenService.issue(user.id());

            return tokenDto;
        } catch (Exception e) {
            throw new LoginFailed();
        }
    }

    private User createUser(String name, String email) {
        User googleUser = User.builder()
                .info(UserInfo.of(name))
                .email(Email.of(email))
                .loginType(LoginType.GOOGLE)
                .build();

        User saved = userRepository.save(googleUser);

        return saved;
    }

    public GoogleInfoResponse getGoogleUserInformation(GoogleResponse googleTokenResponse) {
        String googleToken = googleTokenResponse.id_token();

        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> map = new HashMap<>();

        map.put("id_token", googleToken);

        GoogleInfoResponse userInformationResponse = restTemplate
                .postForEntity("https://oauth2.googleapis.com/tokeninfo", map, GoogleInfoResponse.class)
                .getBody();

        return userInformationResponse;
    }
}
