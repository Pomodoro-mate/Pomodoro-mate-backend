package com.pomodoro.pomodoromate.auth.utils;

import com.pomodoro.pomodoromate.auth.dtos.GoogleRequest;
import com.pomodoro.pomodoromate.auth.dtos.GoogleResponse;
import org.springframework.web.client.RestTemplate;

public class GoogleUtil {
    private final String clientId;
    private final String clientPassword;

    public GoogleUtil(
            String clientId,
            String clientPassword
    ) {
        this.clientId = clientId;
        this.clientPassword = clientPassword;
    }

    public GoogleResponse getGoogleLoginAccessToken(String authCode) {
        RestTemplate restTemplate = new RestTemplate();

        GoogleRequest googleOAuthRequestParam = GoogleRequest.builder()
                .clientId(clientId)
                .clientSecret(clientPassword)
                .code(authCode)
                .redirectUri("http://localhost:8080/api/v1/oauth2/google")
                .grantType("authorization_code")
                .build();

        GoogleResponse googleResponse = restTemplate
                .postForEntity("https://oauth2.googleapis.com/token", googleOAuthRequestParam, GoogleResponse.class)
                .getBody();

        return googleResponse;
    }

    public String getGoogleOAuth2RedirectUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + clientId
                + "&redirect_uri=http://localhost:8080/api/v1/oauth2/google" +
                "&response_type=code&scope=email%20profile%20openid&access_type=offline";
    }
}
