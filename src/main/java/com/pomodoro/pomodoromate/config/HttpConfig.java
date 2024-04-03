package com.pomodoro.pomodoromate.config;

import com.pomodoro.pomodoromate.common.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfig {
    @Value("${same-site}")
    private String sameSite;

    @Bean
    public HttpUtil httpUtil() {
        return new HttpUtil(sameSite);
    }
}
