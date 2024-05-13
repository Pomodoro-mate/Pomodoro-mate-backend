package com.pomodoro.pomodoromate.auth.controllers;

import com.pomodoro.pomodoromate.auth.applications.GoogleLoginService;
import com.pomodoro.pomodoromate.auth.applications.GuestLoginService;
import com.pomodoro.pomodoromate.auth.applications.IssueTokenService;
import com.pomodoro.pomodoromate.auth.dtos.*;
import com.pomodoro.pomodoromate.common.utils.HttpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증 API")
@Slf4j
@RestController
@RequestMapping("api")
public class AuthController {
    private final GuestLoginService guestLoginService;
    private final IssueTokenService issueTokenService;
    private final HttpUtil httpUtil;
    private final GoogleLoginService googleLoginService;

    public AuthController(GuestLoginService guestLoginService,
                          IssueTokenService issueTokenService,
                          HttpUtil httpUtil,
                          GoogleLoginService googleLoginService) {
        this.guestLoginService = guestLoginService;
        this.issueTokenService = issueTokenService;
        this.httpUtil = httpUtil;
        this.googleLoginService = googleLoginService;
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
        log.info("refresh:" + refreshToken);
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

    @Operation(summary = "구글 로그인 페이지 URL")
    @PostMapping("/v1/oauth2/google")
    public String googleLoginUrl() {
        String googleAuthUrl = googleLoginService.getGoogleOAuth2RedirectUrl();

        return googleAuthUrl;
    }

    @Operation(summary = "구글 로그인")
    @GetMapping("/v1/oauth2/google")
    public ResponseEntity<LoginResponseDto> googleLogin(
            @RequestParam(value = "code") String authCode
    ) {
        GoogleResponse googleTokenResponse = googleLoginService.getGoogleLoginAccessToken(authCode);

        GoogleInfoResponse userInformationResponse = googleLoginService.getGoogleUserInformation(googleTokenResponse);

        TokenDto token = googleLoginService.login(userInformationResponse);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new LoginResponseDto(token.accessToken()));
    }
}
