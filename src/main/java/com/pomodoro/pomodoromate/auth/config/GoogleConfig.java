package com.pomodoro.pomodoromate.auth.config;

import com.pomodoro.pomodoromate.auth.utils.GoogleUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleConfig {
    @Bean
    public GoogleUtil googleUtil(
            @Value("${google.client.id}") String clientId,
            @Value("${google.client.password}") String clientPassword
    ) {
        return new GoogleUtil(clientId, clientPassword);
    }
}
