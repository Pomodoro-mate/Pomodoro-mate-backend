package com.pomodoro.pomodoromate.auth.config;

import com.pomodoro.pomodoromate.auth.utils.GoogleUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleConfig {
    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.password}")
    private String clientPassword;

    @Bean
    public GoogleUtil googleUtil() {
        return new GoogleUtil(clientId, clientPassword);
    }
}
