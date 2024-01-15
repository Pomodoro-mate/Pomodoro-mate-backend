package com.pomodoro.pomodoromate.common.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public class HttpUtil {
    public ResponseCookie generateHttpOnlyCookie(String cookieName, String cookieValue) {
        return ResponseCookie.from(cookieName, cookieValue)
                .httpOnly(true)
                .path("/")
                .sameSite("None")
                .secure(true)
                .build();
    }

    public void addCookie(ResponseCookie cookie, HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
