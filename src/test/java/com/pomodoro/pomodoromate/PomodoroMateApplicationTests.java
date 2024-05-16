package com.pomodoro.pomodoromate;

import com.pomodoro.pomodoromate.auth.utils.GoogleUtil;
import com.pomodoro.pomodoromate.auth.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Configuration
public
class PomodoroMateApplicationTests {

    @Test
    void contextLoads() {
    }
}
