package com.pomodoro.pomodoromate.auth.controllers;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.pomodoro.pomodoromate.auth.applications.GuestLoginService;
import com.pomodoro.pomodoromate.auth.applications.IssueTokenService;
import com.pomodoro.pomodoromate.auth.config.JwtConfig;
import com.pomodoro.pomodoromate.auth.dtos.TokenDto;
import com.pomodoro.pomodoromate.auth.exceptions.RefreshTokenExpiredException;
import com.pomodoro.pomodoromate.config.HttpConfig;
import com.pomodoro.pomodoromate.config.SecurityConfig;
import jakarta.servlet.http.Cookie;
import org.hamcrest.Matchers;
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

    @MockBean
    private IssueTokenService issueTokenService;

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

    @Test
    void tokenReissue() throws Exception {
        String token = "not.expired.token";
        Cookie cookie = new Cookie("refreshToken", token);

        given(issueTokenService.reissue(token))
                .willReturn(TokenDto.fake());

        mockMvc.perform(MockMvcRequestBuilders.post("/token")
                        .cookie(cookie))
                .andExpect(status().isCreated())
                .andExpect(content().string(
                        Matchers.containsString("\"accessToken\"")
                ));
    }
}
