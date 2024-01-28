package com.pomodoro.pomodoromate.participant.cotrollers;

import com.pomodoro.pomodoromate.auth.config.JwtConfig;
import com.pomodoro.pomodoromate.auth.exceptions.UnauthorizedException;
import com.pomodoro.pomodoromate.auth.utils.JwtUtil;
import com.pomodoro.pomodoromate.config.SecurityConfig;
import com.pomodoro.pomodoromate.participant.applications.ParticipateService;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParticipantController.class)
@Import({SecurityConfig.class, JwtConfig.class})
class ParticipantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParticipateService participateService;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    void participate() throws Exception {
        UserId userId = UserId.of(1L);
        String token = jwtUtil.encode(userId);

        Long studyRoomId = 1L;

        given(participateService.participate(userId, StudyRoomId.of(studyRoomId)))
                .willReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/studyrooms/" + studyRoomId + "/participants")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());
    }
}