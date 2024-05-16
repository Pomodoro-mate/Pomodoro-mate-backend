package com.pomodoro.pomodoromate;


import com.pomodoro.pomodoromate.config.GoogleConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {GoogleConfig.class})
@TestPropertySource(locations = "/src/main/resources/application.properties")
class PomodoroMateApplicationTests {

    @Test
    void contextLoads() {
    }

}
