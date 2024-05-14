package com.pomodoro.pomodoromate.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GoogleConfig {
    @Value("${google.client.id}")
    private String clientId;
    @Value("${google.client.password}")
    private String clientPassword;

    @Bean
    public String clientId() {
        log.debug("Creating bean for clientId: {}", clientId);
        return clientId;
    }

    @Bean
    public String clientPassword() {
        log.debug("Creating bean for clientPassword");
        return clientPassword;
    }
}
