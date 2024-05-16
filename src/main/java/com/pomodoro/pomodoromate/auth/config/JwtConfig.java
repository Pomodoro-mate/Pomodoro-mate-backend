package com.pomodoro.pomodoromate.auth.config;

import com.pomodoro.pomodoromate.auth.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${access-token.validation-second}")
    private Long accessTokenValidationSecond;

    @Value("${refresh-token.validation-second}")
    private Long refreshTokenValidationSecond;

    @ConditionalOnMissingBean
    public JwtUtil jwtUtil() {
        return new JwtUtil(secret,
                accessTokenValidationSecond,
                refreshTokenValidationSecond);
    }
}
