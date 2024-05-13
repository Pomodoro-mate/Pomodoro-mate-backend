package com.pomodoro.pomodoromate;

import com.pomodoro.pomodoromate.config.GoogleConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {GoogleConfig.class})
class PomodoroMateApplicationTests {

	@Test
	void contextLoads() {
	}

}
