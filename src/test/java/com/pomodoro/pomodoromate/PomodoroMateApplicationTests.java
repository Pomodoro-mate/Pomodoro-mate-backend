package com.pomodoro.pomodoromate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = PomodoroMateApplicationTests.class)
@ActiveProfiles("test")
class PomodoroMateApplicationTests {

    @Test
    void contextLoads() {
    }

}
