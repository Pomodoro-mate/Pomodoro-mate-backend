package com.pomodoro.pomodoromate.config;

import com.pomodoro.pomodoromate.common.utils.HttpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfig {
    @Bean
    public HttpUtil httpUtil() {
        return new HttpUtil();
    }
}
