package com.pomodoro.pomodoromate.common.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public class HttpUtil {
    @Value("${same-site}")
    private String sameSite;

    public ResponseCookie generateHttpOnlyCookie(String cookieName, String cookieValue) {
        return ResponseCookie.from(cookieName, cookieValue)
                .httpOnly(true)
                .path("/")
                .sameSite(sameSite)
                .build();
    }

    public void addCookie(ResponseCookie cookie, HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
