package com.pomodoro.pomodoromate.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleConfig {
    @Value("${google.client.id}")
    private String clientId;
    @Value("${google.client.password}")
    private String clientPassword;

    public String clientId() {
        return clientId;
    }

    public String clientPassword() {
        return clientPassword;
    }
}
