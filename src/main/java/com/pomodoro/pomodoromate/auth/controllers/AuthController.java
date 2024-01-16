package com.pomodoro.pomodoromate.auth.controllers;

import com.pomodoro.pomodoromate.auth.applications.GuestLoginService;
import com.pomodoro.pomodoromate.auth.dtos.LoginResponseDto;
import com.pomodoro.pomodoromate.auth.dtos.TokenDto;
import com.pomodoro.pomodoromate.common.utils.HttpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 API")
@RestController
public class AuthController {
    private final GuestLoginService guestLoginService;
    private final HttpUtil httpUtil;

    public AuthController(GuestLoginService guestLoginService,
                          HttpUtil httpUtil) {
        this.guestLoginService = guestLoginService;
        this.httpUtil = httpUtil;
    }

    @Operation(summary = "게스트 로그인")
    @PostMapping("auth/guest")
    public ResponseEntity<LoginResponseDto> guestLogin(
            HttpServletResponse response
    ) {
        TokenDto token = guestLoginService.login();

        ResponseCookie cookie = httpUtil.generateHttpOnlyCookie("refreshToken", token.refreshToken());

        httpUtil.addCookie(cookie, response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new LoginResponseDto(token.accessToken()));
    }

    @Operation(summary = "로그아웃")
    @DeleteMapping("logout")
    public ResponseEntity<Void> logout(
            HttpServletResponse response
    ) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }
}
