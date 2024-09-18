package com.pomodoro.pomodoromate.auth.config;

import com.pomodoro.pomodoromate.auth.utils.KakaoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoConfig {
    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.client-id}")
    private String clientId;

    @Bean
    public KakaoUtil kakaoUtil() {
        return new KakaoUtil(redirectUri, clientId);
    }
}
