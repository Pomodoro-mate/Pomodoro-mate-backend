package com.pomodoro.pomodoromate.auth.config;

import com.pomodoro.pomodoromate.auth.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(secret);
    }
}
