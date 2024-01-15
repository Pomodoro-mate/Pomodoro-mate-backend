package com.pomodoro.pomodoromate.auth.controllers;

import com.pomodoro.pomodoromate.auth.applications.GuestLoginService;
import com.pomodoro.pomodoromate.auth.config.JwtConfig;
import com.pomodoro.pomodoromate.auth.utils.JwtUtil;
import com.pomodoro.pomodoromate.config.HttpConfig;
import com.pomodoro.pomodoromate.auth.dtos.TokenDto;
import com.pomodoro.pomodoromate.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, HttpConfig.class, JwtConfig.class})
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GuestLoginService guestLoginService;

    @Test
    void guestLogin() throws Exception {
        given(guestLoginService.login())
                .willReturn(TokenDto.fake());

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/guest"))
                .andExpect(status().isCreated())
                .andExpect(content().string(
                        containsString("\"accessToken\"")
                ));
    }
}
