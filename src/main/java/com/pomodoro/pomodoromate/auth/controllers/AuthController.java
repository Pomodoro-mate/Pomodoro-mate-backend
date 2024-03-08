package com.pomodoro.pomodoromate.auth.controllers;

import com.pomodoro.pomodoromate.auth.applications.GuestLoginService;
import com.pomodoro.pomodoromate.auth.applications.IssueTokenService;
import com.pomodoro.pomodoromate.auth.dtos.CreateGuestRequest;
import com.pomodoro.pomodoromate.auth.dtos.CreateGuestRequestDto;
import com.pomodoro.pomodoromate.auth.dtos.LoginResponseDto;
import com.pomodoro.pomodoromate.auth.dtos.ReissuedTokenDto;
import com.pomodoro.pomodoromate.auth.dtos.TokenDto;
import com.pomodoro.pomodoromate.common.utils.HttpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 API")
@RestController
@RequestMapping("api")
public class AuthController {
    private final GuestLoginService guestLoginService;
    private final IssueTokenService issueTokenService;
    private final HttpUtil httpUtil;

    public AuthController(GuestLoginService guestLoginService,
                          IssueTokenService issueTokenService,
                          HttpUtil httpUtil) {
        this.guestLoginService = guestLoginService;
        this.issueTokenService = issueTokenService;
        this.httpUtil = httpUtil;
    }

    @Operation(summary = "게스트 로그인")
    @PostMapping("auth/guest")
    public ResponseEntity<LoginResponseDto> guestLogin(
            HttpServletResponse response,
            @Validated @RequestBody CreateGuestRequestDto requestDto
    ) {
        CreateGuestRequest request = CreateGuestRequest.of(requestDto);

        TokenDto token = guestLoginService.login(request);

        ResponseCookie cookie = httpUtil.generateHttpOnlyCookie("refreshToken", token.refreshToken());

        httpUtil.addCookie(cookie, response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new LoginResponseDto(token.accessToken()));
    }

    @Operation(summary = "accessToken & refreshToken 재발급")
    @PostMapping("token")
    public ResponseEntity<ReissuedTokenDto> reissueToken(
            HttpServletResponse response,
            @CookieValue(value = "refreshToken") String refreshToken
    ) {
        TokenDto token = issueTokenService.reissue(refreshToken);

        ResponseCookie cookie = httpUtil.generateHttpOnlyCookie("refreshToken", token.refreshToken());

        httpUtil.addCookie(cookie, response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ReissuedTokenDto(token.accessToken()));
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
